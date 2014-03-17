/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.waveprotocol.box.webclient.client.atmosphere;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;

import org.waveprotocol.box.webclient.client.WaveWebSocketClient;
import org.waveprotocol.box.webclient.client.events.Log;

/**
 * The wrapper implementation of the atmosphere
 * javascript client.
 *
 * https://github.com/Atmosphere/atmosphere/wiki/jQuery.atmosphere.js-atmosphere.js-API
 *
 * @author pablojan@gmail.com (Pablo Ojanguren)
 *
 */
public class AtmosphereConnectionImpl implements AtmosphereConnection {


      private static final class AtmosphereSocket extends JavaScriptObject {
        public static native AtmosphereSocket create(AtmosphereConnectionImpl impl, String urlBase) /*-{

      var client = $wnd.atmosphere;

                var atsocket = {
                    request: null,
                    socket: null };

                var hasProtocol = urlBase.indexOf("://");
                if (hasProtocol != -1) {
                 urlBase = 'http://'+urlBase.substring(hasProtocol+3);
                }
                else {
                  urlBase = 'http://'+urlBase;
                }

                var connectionUrl = urlBase;

                if (urlBase.charAt(connectionUrl.length-1) == '/') {
                  connectionUrl += 'atmosphere';
                } else {
                  connectionUrl += '/atmosphere';
                }

                //console.log("Connection URL is "+urlBase);
                atsocket.request = new client.AtmosphereRequest();
                atsocket.request.url = connectionUrl;
                atsocket.request.contenType = 'application/json';
                atsocket.request.transport = 'long-polling';
                atsocket.request.fallbackTransport = 'polling';

                atsocket.request.onOpen = $entry(function() {
                    impl.@org.waveprotocol.box.webclient.client.atmosphere.AtmosphereConnectionImpl::onConnect()();
                });

                atsocket.request.onMessage =  $entry(function(response) {

                  var r = response.responseBody;

                  if (r.indexOf('|') == 0) {

                      while (r.indexOf('|') == 0 && r.length > 1) {

                        r = r.substring(1);
                        var marker = r.indexOf('}|');
                        impl.@org.waveprotocol.box.webclient.client.atmosphere.AtmosphereConnectionImpl::onMessage(Ljava/lang/String;)(r.substring(0, marker+1));
                        r = r.substring(marker+1);

                     }

                  }
                  else {

                    impl.@org.waveprotocol.box.webclient.client.atmosphere.AtmosphereConnectionImpl::onMessage(Ljava/lang/String;)(r);

                  }

                });

                atsocket.request.onClose = $entry(function(response) {
                impl.@org.waveprotocol.box.webclient.client.atmosphere.AtmosphereConnectionImpl::onDisconnect(Ljava/lang/String;)(response);
                });


                return atsocket;


        }-*/;

        protected AtmosphereSocket() {
    }


    public native void close() /*-{ this.socket.unsubscribe(); }-*/;

    public native AtmosphereSocket connect() /*-{ this.socket = $wnd.atmosphere.subscribe(this.request);  }-*/;

    public native void send(String data) /*-{ this.socket.push(data); }-*/;
    }


    private final AtmosphereConnectionListener listener;
    private String urlBase;
    private AtmosphereConnectionState state;
    private AtmosphereSocket socket = null;

    public AtmosphereConnectionImpl(AtmosphereConnectionListener listener,
               String urlBase) {
        this.listener = listener;
        this.urlBase = urlBase;

    }


    @Override
    public void connect() {
        if (socket == null) {

                ScriptInjector.fromUrl("/atmosphere/atmosphere.js").setCallback(
                        new Callback<Void, Exception>() {
                                public void onFailure(Exception reason) {
                                        throw new IllegalStateException("atmosphere.js load failed!");
                                }
                                public void onSuccess(Void result) {

                                        socket = AtmosphereSocket.create(AtmosphereConnectionImpl.this, urlBase);
                                        socket.connect();
                                }
                        }).setWindow(ScriptInjector.TOP_WINDOW).inject();
        } else {


          if (AtmosphereConnectionState.CLOSED.equals(this.state))
            socket.connect();

        }
    }

    @Override
    public void close() {
        if (!AtmosphereConnectionState.CLOSED.equals(this.state))
                socket.close();

    }


    @Override
    public void sendMessage(String message) {
        socket.send(message);
    }



    @SuppressWarnings("unused")
    private void onConnect() {
        this.state = AtmosphereConnectionState.OPENING;
        listener.onConnect();
    }

    @SuppressWarnings("unused")
    private void onDisconnect(String response) {
      this.state = AtmosphereConnectionState.CLOSED;
      listener.onDisconnect();
    }

    @SuppressWarnings("unused")
    private void onMessage(String message) {
      listener.onMessage(message);
    }


}

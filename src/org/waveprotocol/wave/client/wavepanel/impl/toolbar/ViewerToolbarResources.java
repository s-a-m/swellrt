/**
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.waveprotocol.wave.client.wavepanel.impl.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Icons for the view toolbar.
 *
 * @author vjrj@ourproject.org (Vicente J. Ruiz Jurado)
 */
public interface ViewerToolbarResources extends ClientBundle {
  interface Css extends CssResource {
    String next();
    String nextUnread();
    String recent();
    String previous();
  }

  @Source("images/view/next.png") ImageResource next();
  @Source("images/view/nextUnread.png") ImageResource nextUnread();
  @Source("images/view/recent.png") ImageResource recent();
  @Source("images/view/previous.png") ImageResource previous();

  @Source("ViewToolbar.css")
  Css css();

  class Loader {
    final static ViewerToolbarResources res = GWT.create(ViewerToolbarResources.class);

    static {
      StyleInjector.inject(res.css().getText(), true);
    }
  }
}

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

package org.waveprotocol.wave.client.wavepanel.impl.toolbar;

import org.waveprotocol.wave.client.wavepanel.impl.focus.FocusBlipSelector;
import org.waveprotocol.wave.client.wavepanel.impl.focus.FocusFramePresenter;
import org.waveprotocol.wave.client.wavepanel.impl.focus.ViewTraverser;
import org.waveprotocol.wave.client.wavepanel.impl.reader.Reader;
import org.waveprotocol.wave.client.wavepanel.view.BlipView;
import org.waveprotocol.wave.client.wavepanel.view.dom.ModelAsViewProvider;
import org.waveprotocol.wave.client.widget.toolbar.ToolbarButtonViewBuilder;
import org.waveprotocol.wave.client.widget.toolbar.ToolbarView;
import org.waveprotocol.wave.client.widget.toolbar.ToplevelToolbarWidget;
import org.waveprotocol.wave.client.widget.toolbar.buttons.ToolbarClickButton;
import org.waveprotocol.wave.model.conversation.ConversationView;

/**
 * Attaches actions that can be performed in a Wave's "view mode" to a toolbar.
 *
 * @author kalman@google.com (Benjamin Kalman)
 */
public final class ViewToolbar {

  private final ToplevelToolbarWidget toolbarUi;
  private final FocusFramePresenter focusFrame;
  private final FocusBlipSelector blipSelector;
  private final Reader reader;
  private final ViewerToolbarResources.Css css;

  private ViewToolbar(ViewerToolbarResources.Css css, ToplevelToolbarWidget toolbarUi, FocusFramePresenter focusFrame,
      ModelAsViewProvider views, ConversationView wave, Reader reader) {
    this.css = css;
    this.toolbarUi = toolbarUi;
    this.focusFrame = focusFrame;
    this.reader = reader;
    blipSelector = FocusBlipSelector.create(wave, views, reader, new ViewTraverser());
  }

  public static ViewToolbar create(FocusFramePresenter focus,  ModelAsViewProvider views,
  ConversationView wave, Reader reader) {
    ViewerToolbarResources.Css css = ViewerToolbarResources.Loader.res.css();
    return new ViewToolbar(css, new ToplevelToolbarWidget(), focus, views, wave, reader);
  }

  public void init() {
    ToolbarView group = toolbarUi.addGroup();


    new ToolbarButtonViewBuilder().setIcon(css.nextUnread()).setTooltip("Next Unread").applyTo(
        group.addClickButton(), new ToolbarClickButton.Listener() {
          @Override
          public void onClicked() {
            BlipView focusedBlip = focusFrame.getFocusedBlip();
            if (focusedBlip == null) {
              focusedBlip = blipSelector.getOrFindRootBlip();
              boolean isRead = reader.isRead(focusedBlip);
              focusFrame.focus(focusedBlip);
              if (isRead) {
                focusFrame.focusNext();
              }
            } else {
              focusFrame.focusNext();
            }
          }
        });
    new ToolbarButtonViewBuilder().setIcon(css.previous()).setTooltip("Previous").applyTo(
        group.addClickButton(), new ToolbarClickButton.Listener() {
          @Override
          public void onClicked() {
            focusFrame.moveUp();
          }
        });
    new ToolbarButtonViewBuilder().setIcon(css.next()).setTooltip("Next").applyTo(
        group.addClickButton(), new ToolbarClickButton.Listener() {
          @Override
          public void onClicked() {
            focusFrame.moveDown();
          }
        });
    new ToolbarButtonViewBuilder().setIcon(css.recent()).setTooltip("Recent").applyTo(
        group.addClickButton(), new ToolbarClickButton.Listener() {
          @Override
          public void onClicked() {
            focusFrame.focus(blipSelector.selectMostRecentlyModified());
          }
        });
    // Fake group
    group = toolbarUi.addGroup();
    new ToolbarButtonViewBuilder().setText("").applyTo(group.addClickButton(), null);
  }

  /**
   * Adds a click button to the toolbar.
   */
  public void addClickButton(String iconCss, ToolbarClickButton.Listener listener) {
    new ToolbarButtonViewBuilder().setIcon(iconCss).applyTo(toolbarUi.addClickButton(), listener);
  }

  /**
   * @return the {@link ToplevelToolbarWidget} backing this toolbar.
   */
  public ToplevelToolbarWidget getWidget() {
    return toolbarUi;
  }
}

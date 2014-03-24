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

package org.waveprotocol.box.webclient.search;

import cc.kune.initials.InitialsAvatarListHelper;

import org.waveprotocol.wave.client.account.Profile;
import org.waveprotocol.wave.client.account.ProfileManager;
import org.waveprotocol.wave.client.common.util.DateUtils;
import org.waveprotocol.wave.client.events.Log;
import org.waveprotocol.wave.model.wave.ParticipantId;

import java.util.LinkedList;
import java.util.List;

/**
 * Renders a digest model into a digest view.
 *
 * @author hearnden@google.com (David Hearnden)
 */
public final class SearchPanelRenderer {

  private static final Log LOG = Log.get(SearchPanelRenderer.class);

  /** Profile provider, for avatars. */
  private final ProfileManager profiles;

  public SearchPanelRenderer(ProfileManager profiles) {
    this.profiles = profiles;
  }

  /**
   * Renders a digest model into a digest view.
   */
  public void render(Digest digest, DigestView digestUi) {
    List<Profile> avatars = new LinkedList<Profile>();
    if (digest.getAuthor() != null) {
      avatars.add(profiles.getProfile(digest.getAuthor()));
    }
    LOG.info("Add author to avatars: " + profiles.getProfile(digest.getAuthor()).getAddress());
    for (ParticipantId other : digest.getParticipantsSnippet()) {
      LOG.info("Add profile to avatars: " + profiles.getProfile(other).getAddress());
      avatars.add(profiles.getProfile(other));
    }

    /* We put the author first and pick three other participant (in the future, maybe randomly) */
    digestUi.setAvatars(InitialsAvatarListHelper.getFourAndSwap(avatars));
    digestUi.setTitleText(digest.getTitle());
    digestUi.setSnippet(digest.getSnippet());
    digestUi.setMessageCounts(digest.getUnreadCount(), digest.getBlipCount());
    digestUi.setTimestamp(
        DateUtils.getInstance().formatPastDate((long) digest.getLastModifiedTime()));
  }
}

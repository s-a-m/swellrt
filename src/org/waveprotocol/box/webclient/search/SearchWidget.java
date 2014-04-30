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

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import org.waveprotocol.wave.client.common.util.QuirksConstants;

import java.util.Date;

/**
 * Widget implementation of the search area.
 *
 * @author hearnden@google.com (David Hearnden)
 */
public class SearchWidget extends Composite implements SearchView, ChangeHandler {

  /** Resources used by this widget. */
  interface Resources extends ClientBundle {
    /** CSS */
    @Source("Search.css")
    Css css();
  }

  interface Css extends CssResource {

    String self();

    String query();

    String searchTextBox();

    String searchDateBox();

    String searchInline();

    String searchLabel();

    String searchContentRow();

    String searchButtonRow();

    String searchButton();

  }

  @UiField(provided = true)
  final static Css css = SearchPanelResourceLoader.getSearch().css();

  interface Binder extends UiBinder<DisclosurePanel, SearchWidget> {
  }

  private final static Binder BINDER = GWT.create(Binder.class);

  private final static String DEFAULT_QUERY = "in:inbox";

  private final static DateTimeFormat QUERY_DATE_FORMAT = DateTimeFormat
      .getFormat(DATE_FORMAT_PATTERN);


  @UiField
  DisclosurePanel disclosurePanel;
  @UiField
  TextBox query;
  @UiField
  TextBox creators;
  @UiField
  TextBox participants;
  @UiField
  ListBox scope;
  @UiField
  DateBox createFromDate;
  @UiField
  DateBox createToDate;
  @UiField
  DateBox lastmodFromDate;
  @UiField
  DateBox lastmodToDate;

  @UiField
  Button search;
  @UiField
  Button reset;


  private Listener listener;

  /**
   *
   */
  public SearchWidget() {
    initWidget(BINDER.createAndBindUi(this));
    if (QuirksConstants.SUPPORTS_SEARCH_INPUT) {
      query.getElement().setAttribute("type", "search");
      query.getElement().setAttribute("results", "10");
      query.getElement().setAttribute("autosave", "QUERY_AUTO_SAVE");
    }
    // We don't use "All search"
    query.addChangeHandler(this);

    // Configure date boxes format
    DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

    createFromDate.setFormat(new DateBox.DefaultFormat(dateFormat));
    createToDate.setFormat(new DateBox.DefaultFormat(dateFormat));
    lastmodFromDate.setFormat(new DateBox.DefaultFormat(dateFormat));
    lastmodToDate.setFormat(new DateBox.DefaultFormat(dateFormat));

    // Be aware of manual (even empty) dates
    createFromDate.setFireNullValues(true);
    createToDate.setFireNullValues(true);
    lastmodFromDate.setFireNullValues(true);
    lastmodToDate.setFireNullValues(true);

    // Animation
    disclosurePanel.setAnimationEnabled(true);



  }

  protected String buildQueryString() {

    String q;

    if (scope.getSelectedIndex() == 2) // all
      q = "in:all";
    else if (scope.getSelectedIndex() == 1) // shared
      q = "in:shared";
    else if (scope.getSelectedIndex() == 0) // inbox
      // inbox
      q = "in:inbox";
    else
      q = DEFAULT_QUERY;

    if (!creators.getValue().isEmpty()) q += " creator:" + creators.getValue();

    if (!participants.getValue().isEmpty()) q += " with:" + participants.getValue();

    // Only one type of date filter can be used
    // UI will force to set only one type, setting empty fields
    if (createFromDate.getValue() != null || createToDate.getValue() != null) {

      q += " usedate:" + FIELD_CREATE_DATE;

      String from =
          createFromDate.getValue() != null ? QUERY_DATE_FORMAT.format(createFromDate.getValue())
              : null;
      if (from != null) q += " from:" + from;

      String to =
          createToDate.getValue() != null ? QUERY_DATE_FORMAT.format(createToDate.getValue())
              : null;
      if (to != null) q += " to:" + to;

    } else if (lastmodFromDate.getValue() != null || lastmodToDate.getValue() != null) {

      q += " usedate:" + FIELD_LAST_MOD_DATE;

      String from =
          lastmodFromDate.getValue() != null ? QUERY_DATE_FORMAT.format(lastmodFromDate.getValue())
              : null;
      if (from != null) q += " from:" + from;

      String to =
          lastmodToDate.getValue() != null ? QUERY_DATE_FORMAT.format(lastmodToDate.getValue())
              : null;
      if (to != null) q += " to:" + to;
    }

    return q;

  }

  protected void resetFields() {
    creators.setValue("", false);
    participants.setValue("", false);
    scope.setSelectedIndex(0);

    createFromDate.setValue(null, false);
    createToDate.setValue(null, false);

    lastmodFromDate.setValue(null, false);
    lastmodToDate.setValue(null, false);
  }

  //
  // UI Events handlers
  //

  @UiHandler("search")
  public void handleSearchOnClick(ClickEvent event) {
    query.setValue(buildQueryString(), false);
    disclosurePanel.setOpen(false);
    onQuery();

  }

  @UiHandler("reset")
  public void handleResetOnClick(ClickEvent event) {
    resetFields();
  }


  /*
   * Both date ranges can't be used at the same time, so date boxes of a range
   * will be cleared if range of other type is populated.
   */

  protected void clearDateBoxValue(DateBox db) {
    db.setValue(null, false);
  }


  @UiHandler("createFromDate")
  public void handleCreateFromDateValueChange(ValueChangeEvent<Date> event) {
    if (event != null) {
      clearDateBoxValue(lastmodFromDate);
      clearDateBoxValue(lastmodToDate);
    }
  }

  @UiHandler("createToDate")
  public void handleCreateToDateValueChange(ValueChangeEvent<Date> event) {
    if (event != null) {
      clearDateBoxValue(lastmodFromDate);
      clearDateBoxValue(lastmodToDate);
    }
  }

  @UiHandler("lastmodFromDate")
  public void handleLastmodFromDateValueChange(ValueChangeEvent<Date> event) {
    if (event != null) {
      clearDateBoxValue(createFromDate);
      clearDateBoxValue(createToDate);
    }
  }

  @UiHandler("lastmodToDate")
  public void handleLastmodToDateValueChange(ValueChangeEvent<Date> event) {
    if (event != null) {
      clearDateBoxValue(createFromDate);
      clearDateBoxValue(createToDate);
    }
  }

  //
  // View interface
  //

  @Override
  public void init(Listener listener) {
    Preconditions.checkState(this.listener == null);
    Preconditions.checkArgument(listener != null);
    this.listener = listener;
  }

  @Override
  public void reset() {
    Preconditions.checkState(listener != null);
    listener = null;
  }

  @Override
  public String getQuery() {
    return query.getValue();
  }

  @Override
  public void setQuery(String text) {
    query.setValue(text);
  }


  /*
   * Handle onChange event for the query input box (non-Javadoc)
   * @see
   * com.google.gwt.event.dom.client.ChangeHandler#onChange(com.google.gwt.event
   * .dom.client.ChangeEvent)
   */
  @Override
  public void onChange(ChangeEvent event) {

    if (query.getValue() == null || query.getValue().isEmpty()) {
      query.setValue(DEFAULT_QUERY);
    }
    onQuery();
  }

  private void onQuery() {
    if (listener != null) {
      listener.onQueryEntered();
    }
  }



}

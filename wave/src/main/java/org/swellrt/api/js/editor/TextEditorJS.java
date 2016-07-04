package org.swellrt.api.js.editor;

import com.google.gwt.core.client.JavaScriptObject;

import org.swellrt.api.WaveClient;
import org.swellrt.client.editor.TextEditor;


public class TextEditorJS extends JavaScriptObject {

  public native static TextEditorJS create(TextEditor delegate, WaveClient client) /*-{

    var jsWrapper = {

      onSelectionChanged: function(handler) {
        var _handler = @org.swellrt.api.js.editor.TextEditorJSListener::create(Lcom/google/gwt/core/client/JavaScriptObject;)(handler);
        delegate.@org.swellrt.client.editor.TextEditor::setListener(Lorg/swellrt/client/editor/TextEditorListener;)(_handler);
      },

      edit: function(text) {

        // TODO check for cleanUp();

        var _text = text.getDelegate();
        client.@org.swellrt.api.WaveClient::configureTextEditor(Lorg/swellrt/client/editor/TextEditor;Lorg/swellrt/model/generic/TextType;)(delegate, _text);
        delegate.@org.swellrt.client.editor.TextEditor::edit(Lorg/swellrt/model/generic/TextType;)(_text);
      },

      cleanUp: function() {
        delegate.@org.swellrt.client.editor.TextEditor::cleanUp()();
        return this;
      },

      setEditing: function(editing) {
        delegate.@org.swellrt.client.editor.TextEditor::setEditing(Z)(editing);
        return this;
      },

      toggleDebug: function() {
        delegate.@org.swellrt.client.editor.TextEditor::toggleDebug()();
      },

      addWidget: function(name, state) {
        return delegate.@org.swellrt.client.editor.TextEditor::addWidget(Ljava/lang/String;Ljava/lang/String;)(name,state);
      },

      setAnnotation: function(name, value) {
         delegate.@org.swellrt.client.editor.TextEditor::setAnnotation(Ljava/lang/String;Ljava/lang/String;)(name, value);
      },

      getSelection: function() {
        return delegate.@org.swellrt.client.editor.TextEditor::getSelection()();
      }

    }; // jsWrapper

    return jsWrapper;

  }-*/;


  protected TextEditorJS() {

  }

}

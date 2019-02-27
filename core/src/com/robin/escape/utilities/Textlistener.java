package com.robin.escape.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Textlistener implements Input.TextInputListener {
    String inputText;
    String header;

    @Override
    public void input (String text) {
        inputText = text;
    }

    public void setHeader(String header){ this.header = header; }
    public String getHeader(){ return header; }
    public String getInputText(){ return inputText; }
    public void resetInputText() { inputText = null; }

    @Override
    public void canceled () {
    }
}


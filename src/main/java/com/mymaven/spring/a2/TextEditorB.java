package com.mymaven.spring.a2;

public class TextEditorB {

    private SpellChecker spellChecker;

    public void setSpellChecker(SpellChecker spellChecker) {
        System.out.println("Inside setSpellChecker");
        this.spellChecker = spellChecker;
    }

    public SpellChecker getSpellChecker() {
        System.out.println("Inside getSpellChecker");
        return spellChecker;
    }

    public void spellCheck() {
        spellChecker.checkSpelling();
    }
}

package com.mymaven.spring.a2;

public class TextEditor {

    private SpellChecker spellChecker;
    private SpellCheckerB spellCheckerB;

    public TextEditor(SpellChecker spellChecker, SpellCheckerB spellCheckerB) {
        System.out.println("Inside TextEditor constructor.");
        this.spellChecker = spellChecker;
        this.spellCheckerB = spellCheckerB;
    }

    public void spellCheck() {
        spellChecker.checkSpelling();
        spellCheckerB.checkSpelling();
    }
}

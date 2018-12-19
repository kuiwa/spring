package com.mymaven.game.hero;

import org.testng.annotations.Test;

public class HeroTest {

    @Test
    public void runTest() {
        Hero garen = new Hero();
        garen.name = "Garen";
        garen.hp = 616.28f;
        garen.armor = 27.536f;
        garen.moveSpeed = 350;
        garen.damage = 80;

        Hero teemo = new Hero();
        teemo.name = "Teemo";
        teemo.hp = 383f;
        teemo.armor = 14f;
        teemo.moveSpeed = 330;
        teemo.damage = 40;

        Item bloodBottle = new Item();
        bloodBottle.name = "BloodBottle";
        bloodBottle.price = 50;

        Item strawShoes = new Item();
        strawShoes.name = "StrawShoes";
        strawShoes.price = 300;
        
        filter(garen, h->h.hp>100 && h.damage>50);

    }
    
    private void filter(Hero hero, HeroChecker heroChecker) {
        if (heroChecker.test(hero))
            System.out.println(hero.name);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bdg.spacewar.player;

import bdg.spacewar.SpacewarPlayer;

/**
 *
 * @author Bence
 */
public class Player implements SpacewarPlayer{
    
    public Player() {
        System.out.println("I have been instantiated!");
    }

    @Override
    public String round(String csv) {
        System.out.println("Round method was called "+csv);
        return "";
    }

    @Override
    public void configuration(String csv) {
        System.out.println("Configuration method was called "+csv);
    }
    
}

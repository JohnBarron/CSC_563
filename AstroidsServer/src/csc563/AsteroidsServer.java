package csc563;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Renu
 */

import java.io.*;
 
public class AsteroidsServer {
    
    public AsteroidsServer() throws IOException {
        
        new MulticastAsteroidsServerThread().start();
        
    }
}
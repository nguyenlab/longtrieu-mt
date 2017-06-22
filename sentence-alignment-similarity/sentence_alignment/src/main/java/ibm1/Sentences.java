/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm1;

import java.util.ArrayList;

/**
 *
 * @author Am
 */
public class Sentences {
      private int numberOfWord;
      private ArrayList<String>  vecto;
    
   public Sentences (){
       this.numberOfWord=0;
       this.vecto=new ArrayList<String>();
   }
      public Sentences (Sentences x){
       this.numberOfWord=x.numberOfWord;
       this.vecto=x.vecto;
   }
       
    public void setNumberOfWord(int x){
        this.numberOfWord=x;
    }
    
     public int getNumberOfWord(){
         return this.numberOfWord;
     }
        
    
   
    
    public ArrayList<String> getVecto(){
       return vecto;
    }     
    
    
    public String getVecto(int i){


       return this.vecto.get(i);
             
    }

    /**
     * @param vecto the vecto to set
     */
    public void setVecto(ArrayList<String> vecto) {
        this.vecto = vecto;
    }
    
    
   
}
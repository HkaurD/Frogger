// Mr McKenzie's code for sound effects
import java.io.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

public class SoundEffect {
    private Clip c;

    public SoundEffect(String filename){
        setClip(filename);
    } // taking in the audio wav file's name

    // this method takes in an audio's file name as a parameter and opens it so it can be used
    public void setClip(String filename){
        // this gets the audio file clip and opens the audio for when its called to play later in the code
        try{
            File f = new File(filename);
            c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(f));
        }
        catch(Exception e){ // if the audio file could not be found or opened
            System.out.println(filename + "error");
        }
    }

    // this method is used to play the sound effect; it starts from the beginning
    public void play(){
        c.setFramePosition(0);
        c.start();
    }
}
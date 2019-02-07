package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

import static java.lang.System.exit;

/**
 * Created by Boris.InspiratGames on 2/03/18.
 */

public class SoundMusicMation {

    //NAME CONVENTION:
    // MUSICname OR SOUNDname + LEVELnumber;
    public static final String TAG=SoundMusicMation.class.getSimpleName();
    HashMap<String,Sound> soundMation;
    Array<String> sounds;
    String pathM="music/";
    String pathS="sound/";
    HashMap<String,SoundMusic> musicMation;
    Array<String> musics;
    GenericMethodsInputProcessor g;
    public HashMap<String,Long> soundInstances;

    public SoundMusicMation(){

    }
    public Music loadMusic(String nameMusicPath, boolean isMusic) {
        if (musicMation == null) musicMation = new HashMap<String, SoundMusic>();
        SoundMusic m = new SoundMusic();
        if(musicMation.get(nameMusicPath)==null){
            m.msm=Gdx.audio.newMusic ( Gdx.files.internal (nameMusicPath ));
            musicMation.put(nameMusicPath,m);
            m.isMusic = isMusic;
        }
        else m=musicMation.get(nameMusicPath);
        if(g!=null) g.println(TAG + " CHARGING...at PATH: "+ nameMusicPath);
        else System.out.println(TAG + " CHARGING...at PATH: "+ nameMusicPath);
        return m.msm;

    }

    public void pauseMusic (String nameMusic){
        Music m = musicMation.get(nameMusic).msm;
        m.pause();
    }

    public Music loadMusic(String music,String level) {
        if (musicMation == null) musicMation = new HashMap<String, SoundMusic>();
        String nameMusic = music + level;
        String nameMusicPath = pathM + nameMusic + ".ogg";
        return loadMusic(nameMusicPath, true);
    }

    public void setMusicVolume(String withname, float volume){
        SoundMusic msc = musicMation.get(withname);
        msc.volume=volume;
        msc.msm.setVolume(msc.volume);
    }


    public Sound loadSound(String nameSoundPath){
        if(soundMation==null)
            soundMation=new HashMap<String, Sound>();
        Sound s;
        if(soundMation.get(nameSoundPath)==null){
            s=Gdx.audio.newSound ( Gdx.files.internal ( nameSoundPath ));
            soundMation.put(nameSoundPath,s);
        }
        else s=soundMation.get(nameSoundPath);
        if(g!=null) g.println(TAG + " CHARGING...at PATH: "+ nameSoundPath);
        else System.out.println(TAG + " CHARGING...at PATH: "+ nameSoundPath);
        return s;
    }

    public Sound loadSound(String sound, String level){
        if(soundMation==null)
            soundMation=new HashMap<String, Sound>();
        String nameSound=sound+level;
        String nameSoundPath=pathS+nameSound+".ogg";
        return loadSound(nameSoundPath);
    }

    public void playSound(String withnameSound,Sound snd){
        playSound( withnameSound, snd, false , 1);
    }



    public void playSound(String withnameSound,Sound snd, float volume){
        playSound( withnameSound, snd, false , volume);
    }

    public void playSound(String withnameSound,Sound snd, boolean loop){
        playSound( withnameSound, snd, loop , 1);
    }

    public void playSound(String withnameSound,Sound snd,boolean loop,float volume){
        if(soundInstances==null)
            soundInstances=new HashMap<String, Long>();
        Long id=snd.play();
        snd.setLooping(id,loop);
        soundInstances.put(withnameSound,id);
    }

    public void playMusic(String withname, boolean loop){
        Music m = musicMation.get(withname).msm;

        if(m.getVolume()==0.0f && g.musicMute==false){
            musicMation.get(withname).volume = 0.7f;
            m.setVolume(musicMation.get(withname).volume);
        }else if(g.musicMute){
            m.setVolume(0.0f);
        }

        m.setLooping(loop);
        m.play();

    }

    public Long getSoundId(String withnameSound){
        if(soundInstances==null)
            return null;
        else return soundInstances.get(withnameSound);
    }

    public void disposeSound(String nameSound){
        Sound snd=soundMation.get(nameSound);
        snd.stop();
        soundMation.remove(nameSound);
        snd.dispose();
    }

    public void disposeMusic(String nameMusic){
        Music msc=musicMation.get(nameMusic).msm;
        msc.stop();
        musicMation.remove(nameMusic);
        msc.dispose();
    }

    public void loadAllLevelMusicAndSounds(int lvl){
        String level="LVL-"+String.valueOf(lvl);
        for(String music:musics){
            if (music.contains(level));
            loadMusic(music, true);
        }
        for(String sound:sounds){
            if (sound.contains(level));
            loadSound(sound);
        }
    }

    public Music getMusic(String withname){
        return musicMation.get(withname).msm;
    }

    public void disposeAllLevelMusicAndSounds(int lvl){
        String level="LVL-"+String.valueOf(lvl);
        for(String music:musics){
            if (music.contains(level));
            disposeMusic(music);
        }
        for(String sound:sounds){
            if (sound.contains(level));
            disposeSound(sound);
        }
    }

    public void pauseAllMusic(){
        for (String music:musicMation.keySet()){
            if (musicMation.get(music)!=null){
                musicMation.get(music).msm.pause();
            }
        }
        /*for (String sound:soundMation.keySet()){
            if(soundMation.get(sound)!=null) soundMation.get(sound).pause();
        }
        */
    }


    public void searchForExist(GenericMethodsInputProcessor g){
        this.g=g;
        if(sounds!=null && musics!=null){
            for(String s:sounds){
                String filename=pathS+s+".mp3";
                if(Gdx.files.internal(filename).exists()){
                    if(g!=null) g.println(TAG + " SOUND: "+s + " AT PATH " + filename +" CONFIRMED FILE.");
                    else System.out.println(TAG + " SOUND: "+s + " AT PATH " + filename +" CONFIRMED FILE.");
                }else{
                    if(g!=null) g.println(TAG + " SOUND: "+s + " AT PATH " + filename +" NOT EXIST. ABORTING.");
                    else System.out.println(TAG + " SOUND: "+s + " AT PATH " + filename +" NOT EXIST. ABORTING.");
                    exit(1);
                }
            }
            for(String s:musics){
                String filename=pathM+s+".mp3";
                if(Gdx.files.internal(filename).exists()){
                    if(g!=null) g.println(TAG + " MUSIC: "+s + " AT PATH " + filename +" CONFIRMED FILE.");
                    else System.out.println(TAG + " MUSIC: "+s + " AT PATH " + filename +" CONFIRMED FILE.");
                }else{
                    if(g!=null) g.println(TAG + " MUSIC: "+s + " AT PATH " + filename +" NOT EXIST. ABORTING.");
                    else System.out.println(TAG + " MUSIC: "+s + " AT PATH " + filename +" NOT EXIST. ABORTING.");
                    exit(1);
                }
            }

        }else{
            System.out.println(TAG + " FILE ERROR");
        }
    }

    public void allMusicVolume() {

        for(String m : musicMation.keySet()){
            if(g.musicMute) musicMation.get(m).msm.setVolume(0);
            else musicMation.get(m).msm.setVolume(musicMation.get(m).volume);
        }
    }


    public class SoundMusic {

        Music msm;
        float volume;
        //Sound ssm;
        boolean isMusic;

        public SoundMusic(){

        }
    }




}
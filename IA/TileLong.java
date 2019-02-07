package com.mygdx.safe.IA;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 22/02/18.
 */

public class TileLong {
    private static final String TAG=TileLong.class.getSimpleName();
    private LongPos parent;
    private Array<LongPos> childs= new Array<LongPos>();

    public TileLong(){
        parent=new LongPos(0);
        for(int i=0;i<64;i++){
            childs.add(new LongPos(0));
        }
    }

    public void fillAll(){
        parent.setLong(-1);
        for(int i=0;i<64;i++){
            childs.get(i).setLong(-1);
        }
    }

    public void clearAll(){
        parent.setLong(0);
        for(int i=0;i<64;i++){
            childs.get(i).setLong(0);
        }
    }

    public void setLong(Long ln,long[] childLn){
        parent.setLong(ln);
        if(parent.size==childLn.length) {
            for (int i = 0; i < parent.size; i++) {
                childs.get(parent.positionArray[i]).setLong(childLn[i]);
            }
        }else{
            System.out.println(TAG + " ERROR!. CHILDS SIZE NOT COMPATIBLE WITH PARENT SIZE");
        }
    }
    public void setLong(Long ln){
        parent.setLong(ln);
        for(int i=0;i<parent.size;i++){
            childs.get(parent.positionArray[i]).setAll();
        }
    }

    public long getLong(){
        return parent.l;
    }



}

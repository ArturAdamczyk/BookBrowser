package com.art.bookbrowser.helpers;

import android.content.Intent;
import android.os.Bundle;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class IntentManager {
    @Getter
    @Setter
    private Bundle intentExtras;
    @Getter @Setter
    private Set<String> intentActions;

    public IntentManager(Bundle intentExtras, Set<String> intentActions){
        this.intentExtras = intentExtras;
        this.intentActions = intentActions;
    }

    public void clear(){
        intentActions.clear();
        intentExtras.clear();
    }

    public Intent feedIntent(Intent intent){
        intent.putExtras(intentExtras);
        for(String action: intentActions) {
            intent.setAction(action);
        }
        return intent;
    }
}

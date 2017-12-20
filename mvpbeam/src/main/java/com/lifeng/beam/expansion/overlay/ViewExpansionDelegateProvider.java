package com.lifeng.beam.expansion.overlay;

import com.lifeng.beam.expansion.BeamBaseActivity;

/**
 * Created by Mr.Jude on 2015/8/17.
 */
public interface ViewExpansionDelegateProvider {
    ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity);

    ViewExpansionDelegateProvider DEFAULT = new ViewExpansionDelegateProvider() {
        @Override
        public ViewExpansionDelegate createViewExpansionDelegate(BeamBaseActivity activity) {
            return new DefaultViewExpansionDelegate(activity);
        }
    };

}

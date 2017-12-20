package com.lifeng.beam.expansion.data;

import com.lifeng.beam.bijection.BeamFragment;
import com.lifeng.beam.bijection.RequiresPresenter;

/**
 * Created by Mr.Jude on 2015/8/20.
 */
@RequiresPresenter(BeamDataActivityPresenter.class)
public class BeamDataFragment<T extends BeamDataFragmentPresenter,M> extends BeamFragment<T> {

    public void setData(M data){}
    public void setError(Throwable e){
        throw new RuntimeException(e);
    }

}

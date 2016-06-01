package com.buycolle.aicang.ui.view.mainScrole;/**
 * Created by cpoopc on 2015/8/28.
 */

import com.buycolle.aicang.ui.fragment.BaseFragment;

/**
 * User: cpoopc
 * Date: 2015-08-28
 * Time: 11:45
 * Ver.: 0.1
 */
public abstract class ScrollAbleFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
    public abstract void refresh(int status);
}

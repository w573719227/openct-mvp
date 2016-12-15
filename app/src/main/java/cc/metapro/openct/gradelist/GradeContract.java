package cc.metapro.openct.gradelist;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

import cc.metapro.openct.BasePresenter;
import cc.metapro.openct.BaseView;
import cc.metapro.openct.data.GradeInfo;
import cc.metapro.openct.utils.ActivityUtils;

/**
 * Created by jeffrey on 16/12/2.
 */

interface GradeContract {

    interface View extends BaseView<Presenter> {

        void showAll(List<GradeInfo> infos);

        void showOnResultFail();

        void showOnResultOk();

        void showOnCodeEmpty();

        void setCAPTCHADialogHelper(ActivityUtils.CaptchaDialogHelper captchaDialogHelper);

        void showOnCAPTCHALoaded(Drawable captcha);

        void showOnCAPTCHAFail();
    }

    interface Presenter extends BasePresenter {

        void loadOnlineGradeInfos(Context context, String code);

        void loadLocalGradeInfos(Context context);

        void loadCAPTCHA();

        void storeGradeInfos(Context context);

        void clearGradeInfos();
    }
}
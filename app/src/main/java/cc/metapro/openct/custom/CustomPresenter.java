package cc.metapro.openct.custom;

/*
 *  Copyright 2016 - 2017 OpenCT open source class table
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import org.jsoup.nodes.Element;

import java.security.GeneralSecurityException;

import cc.metapro.interactiveweb.InteractiveWebView;
import cc.metapro.interactiveweb.utils.HTMLUtils;
import cc.metapro.openct.data.source.local.DBManger;
import cc.metapro.openct.data.source.local.LocalHelper;
import cc.metapro.openct.data.source.local.LocalUser;
import cc.metapro.openct.utils.Constants;

class CustomPresenter implements CustomContract.Presenter {

    private Context mContext;
    private String PASSWORD;
    private String TYPE;
    private LocalUser mUser;

    CustomPresenter(Context context, CustomContract.View view, String type) {
        mContext = context;
        TYPE = type;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        Constants.sDetailCustomInfo = DBManger.getDetailCustomInfo(mContext);
        switch (TYPE) {
            case Constants.TYPE_CLASS:
                mUser = LocalHelper.getCmsStuInfo(mContext);
                break;
            case Constants.TYPE_GRADE:
                mUser = LocalHelper.getCmsStuInfo(mContext);
                break;
            case Constants.TYPE_BORROW:
                mUser = LocalHelper.getLibStuInfo(mContext);
                break;
        }
        try {
            PASSWORD = mUser.getPassword();
        } catch (GeneralSecurityException e) {
            PASSWORD = "";
        }
    }

    @Override
    public void setWebView(final InteractiveWebView webView, final FragmentManager manager) {
        webView.setUserClickCallback(new InteractiveWebView.ClickCallback() {
            @Override
            public void onClick(@NonNull final Element element) {
                if (HTMLUtils.isPasswordInput(element)) {
                    if (!webView.setById(element.id(), "value", PASSWORD)) {
                        webView.setByName(element.attr("name"), "value", PASSWORD);
                    }
                } else if (HTMLUtils.isTextInput(element)) {
                    ClickDialog.newInstance(new ClickDialog.TypeCallback() {
                        @Override
                        public void onResult(String type) {
                            switch (type) {
                                case InteractiveWebView.COMMON_INPUT_FLAG:
                                    if (!webView.focusById(element.id())) {
                                        webView.focusByName(element.attr("name"));
                                    }
                                    break;
                                case InteractiveWebView.USERNAME_INPUT_FLAG:
                                    if (!webView.setById(element.id(), "value", mUser.getUsername())) {
                                        webView.setByName(element.attr("name"), "value", mUser.getUsername());
                                    }
                                    break;
                            }
                        }
                    }).show(manager, "click_dialog");
                }
            }
        });
    }
}

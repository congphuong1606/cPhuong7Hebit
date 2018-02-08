package com.discovertodo.phone.android.ebook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.fragment.EbookFragment;
import com.discovertodo.phone.android.global.DialogLoading;
import com.discovertodo.phone.android.model.TagClassHtml;
import com.discovertodo.phone.android.util.JsonUtils;
import com.discovertodo.phone.android.util.StoreData;

import org.json.JSONArray;

import java.util.ArrayList;

public class EpubWebViewClient extends WebViewClient {

    private Activity activity;
    private EpubWebView webView;
    public static float widthWeb, heightWeb;
    public static int totalWidth;
    private int pageCount = 0;
    public static int[] arrMenu;
    public static int[] arrListTagP, arrListHeightTagP;
    public static int totalHeight;
    private int topTable = 0;
    private int heightTable = 0;

    StoreData data;

    public EpubWebViewClient(Activity activity, EpubWebView webView) {
        this.activity = activity;
        this.webView = webView;

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        MyJavaScriptInterface javaInterface = new MyJavaScriptInterface();
        webView.addJavascriptInterface(javaInterface, "HTMLOUT");
        data = new StoreData(activity);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        widthWeb = view.getMeasuredWidth()
                / activity.getResources().getDisplayMetrics().density;
        heightWeb = view.getMeasuredHeight()
                / activity.getResources().getDisplayMetrics().density;

        String varMySheet = "var mySheet = document.styleSheets[0];";
        String addCSSRule = "function addCSSRule(selector, newRule) {"
                + "if (mySheet.addRule) {"
                + "mySheet.addRule(selector, newRule);"
                + "} else {"
                + "ruleIndex = mySheet.cssRules.length;"
                + "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);"
                + "}" + "}";
        String breakPage = "addCSSRule('p.div-pagebreak', '-webkit-column-break-after: always;')";
        String insertRule2 = "addCSSRule('p', 'text-align: justify;border-style: solid; padding-top:0px; padding-left:20px;  padding-right:15px; ')";
//        String insertRule3 = "addCSSRule('table, th, td', 'margin:5px 20px 5px 20px;')";
//        String insertRule4 = "addCSSRule('p.dmManh', 'padding-top:10px; padding-bottom:10px;')";
//        String setTextSizeRule = "addCSSRule('body', 'font-size: 120%;line-height:1;')";
        String setHighlightColorRule = "addCSSRule('highlight', 'background-color: rgba(0,0,0,0);')";

        EpubWebView.loadJavascript(view, "javascript:" + varMySheet);
        EpubWebView.loadJavascript(view, "javascript:" + addCSSRule);
//        EpubWebView.loadJavascript(view, "javascript:" + breakPage);
        EpubWebView.loadJavascript(view, "javascript:" + insertRule2);
//        EpubWebView.loadJavascript(view, "javascript:" + insertRule3);
//        EpubWebView.loadJavascript(view, "javascript:" + insertRule4);
//        EpubWebView.loadJavascript(view, "javascript:" + setTextSizeRule);
        EpubWebView.loadJavascript(view, "javascript:" + setHighlightColorRule);
        if (!EpubWebView.isScroll) {
            final String insertRule1 = "addCSSRule('html', 'padding: 0px; height: "
                    + heightWeb
                    + "px; -webkit-column-gap: 0px; -webkit-column-width: "
                    + widthWeb + "px;')";
            view.postDelayed(new Runnable() {

                @Override
                public void run() {
                    EpubWebView.loadJavascript(view, "javascript:" + insertRule1);
                }
            }, 300);
        }

        if (EpubWebView.isScroll) {
//			try {
//				//int height = (int) (heightWeb - EpubWebViewClient.arrListTagP[1]);
//				String reSizeImg1 = "var image1 = document.getElementById('image-1');"
//						+ "image.style.width = '"+widthWeb+"px';" +
//						"image.style.height = 'auto';";
//				EpubWebView.loadJavascript(view, "javascript:" + reSizeImg1);
//			} catch (Exception e) {
//			}
        }


        final String[] listFont = activity.getResources().getStringArray(
                R.array.font);
        // final String[] listFont = EbookFragment.getFont();
        String strNameFont = "";
        if (data.getStringValue("font").equalsIgnoreCase("")) {
            strNameFont = listFont[0];
        } else {
            strNameFont = data.getStringValue("font");
        }
        String strRunFont = "allp = document.getElementsByTagName('p');"
                + "for (i = 0; i < allp.length; i++) { tagp = allp[i]; tagp.style.fontFamily = 'FontName';} ";

        final String strAddStyle = "var newStyle = document.createElement( 'style' );"
                + "newStyle.appendChild( document.createTextNode( "
                + "\"@font-face { "
                + "font-family: 'FontName'; "
                + "src: url('"
                + strNameFont
                + "') format('opentype'); "
                + "} \") );"
                + " document.head.appendChild( newStyle ); "
                + strRunFont;
        loadImage(view);

        ArrayList<TagClassHtml> hidetags = new ArrayList<>();
        JSONArray lists = JsonUtils.getListJsonOject(JsonUtils.getJson(activity), "Hidetags");
        hidetags = JsonUtils.getListObject(lists);
        for (TagClassHtml tag : hidetags) {
            String nameClass = tag.getNameClass();
            String positon = String.valueOf(tag.getPosition());
            String hideTag = "var hideTag=document.getElementsByClassName('" + nameClass + "')[" + positon + "];" + "hideTag.style.display='none';";
            EpubWebView.loadJavascript(view, "javascript:" + hideTag);
        }


        String goToOffsetFunc = " function pageScroll(xOffset){ window.scroll(xOffset,0); } ";
        EpubWebView.loadJavascript(view, "javascript:" + goToOffsetFunc);

        final String textSize1 = "document.body.style.fontSize = \""
                + webView.getTextSize()+ "em\"";

        EpubWebView.loadJavascript(view, "javascript:" + textSize1);
        EpubWebView.loadJavascript(view, "javascript:" + strAddStyle);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((EpubWebView) view).updateCountPage();
            }
        }, 500);

    }

    private void loadImage(WebView view) {
        String image[] = new String[52];
//. set hinh ảnh full w
        for (int i = 0; i <= 51; i++) {
            image[i] = "var imagefulls = document.getElementById('image-" + (i + 1) + "');"
                    + "imagefulls.style.width = '" + widthWeb + "px';"
                 +
                    "imagefulls.style.height = 'auto';";
            EpubWebView.loadJavascript(view, "javascript:" + image[i]);
        }

        //set nhwuxng hình ảnh có cỡ nhỏ
        int w = (int) (2 * (widthWeb / 3));
        int w4 = (int) (widthWeb / 4);
        ArrayList<TagClassHtml> images = new ArrayList<>();
        JSONArray listImage = JsonUtils.getListJsonOject(JsonUtils.getJson(activity), "Image");
        images = JsonUtils.getListObject(listImage);
        for (TagClassHtml tag : images) {
            String nameClass = tag.getNameClass();
            String imageSmall = "var imageSmalls = document.getElementById('" + nameClass + "');"
                    + "imageSmalls.style.width = '" + w + "px';"
                   +
                    "imageSmalls.style.height = 'auto';";
            EpubWebView.loadJavascript(view, "javascript:" + imageSmall);

        }
        String image52 = "var hideTag2=document.getElementById('image-52');" + "hideTag2.style.width = '" + (int)(widthWeb/2)+ "px';"
                +
                "hideTag2.style.height = 'auto';";
        EpubWebView.loadJavascript(view, "javascript:" + image52);

    }

    class MyJavaScriptInterface {

        @JavascriptInterface
        public void scrollWidth(String jsResult) {
            totalWidth = Integer.valueOf(jsResult);
        }

        @JavascriptInterface
        public void scrollHeight(String jsResult) {
            totalHeight = Integer.valueOf(jsResult);
            // Log.e("XXX scrollHeight", totalHeight + "");
            if (totalHeight > totalWidth) {
                pageCount = (int) ((float) totalHeight / heightWeb);
            } else {
                pageCount = (int) ((float) totalWidth / widthWeb);
            }

            webView.setTotalPageCount(pageCount);

            if (EpubWebView.isScroll) {
                webView.getEbookFragment().getSeekBarVertical()
                        .setMax(pageCount - 1);
            } else
                webView.getEbookFragment().getSeekBar().setMax(pageCount - 1);

            // webView.goToPage(pageWebview());

            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            webView.setVisibility(View.VISIBLE);
                            DialogLoading.cancel();
                        }
                    });
                }

                ;
            }.start();

        }

        @JavascriptInterface
        public void getTop(int[] value) {
            arrMenu = value;
//			 for (int i = 0; i < arrMenu.length; i++) {
//			 Log.e("DAT", i + "::" + arrMenu[i] + ">>.");
//			 }
        }

        @JavascriptInterface
        public void getTopTagP(int[] value) {
            int count = 0;
            int dem = 0;
            for (int i = 213; i < 252; i++) {
                if (count == 3) {
                    count = 0;
                    dem++;
                }
                value[i] = value[i] + topTable + dem * (heightTable / 13);
                count++;
            }

            arrListTagP = value;
            // for (int i = 0; i < arrListTagP.length; i++) {
            // Log.e("DAT", i + "::" + arrListTagP[i] + ">>.");
            // }
        }

        @JavascriptInterface
        public void getHeightP(int[] value) {
            arrListHeightTagP = value;

            if (EbookFragment.ischeckScroll) {
                webView.goToPage(pageWebview());
                EbookFragment.ischeckScroll = false;
            } else {
                int newCurr = (int) (webView.getCurrentState() * pageCount);
                // if (!EpubWebView.isScroll) {
                webView.goToPage(newCurr);
                // } else {
                // webView.updateView();
                // }
            }
            webView.updateSeekBar();
            webView.dismisDialog();
        }

        @JavascriptInterface
        public void topTable(int value) {
            // Log.e("DAT", "top:" + value);
            topTable = value;
        }

        @JavascriptInterface
        public void heightTable(int value) {
            // Log.e("DAT", "height:" + value);
            heightTable = value;
        }

        @JavascriptInterface
        public void getSearch(int[] value) {
            // for (int i = 0; i < value.length; i++) {
            // Log.e("DAT", i + ":" + value[i] + ">>>");
            // }
        }

    }

    private int pageWebview() {
        int height = 0;
        if (EbookFragment.saveTabP == 0 && EbookFragment.saveIndex == 0)
            return 0;
        if (totalHeight > totalWidth) {
            height = (int) heightWeb;
        } else
            height = totalHeight;

        int page = arrListTagP[EbookFragment.saveTabP] / height;

        if (lenghtTabP() + arrListTagP[EbookFragment.saveTabP] >= height * page) {
            page = page + ((lenghtTabP() / height));
        }
        return page;
    }

    private int lenghtTabP() {
        return Math
                .round(((float) (EbookFragment.saveIndex + 3) / (float) EbookFragment.list
                        .get(EbookFragment.saveTabP))
                        * (float) arrListHeightTagP[EbookFragment.saveTabP]);
    }
}

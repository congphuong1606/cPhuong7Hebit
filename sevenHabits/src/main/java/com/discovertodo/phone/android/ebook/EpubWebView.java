package com.discovertodo.phone.android.ebook;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.ShowImageActivity;
import com.discovertodo.phone.android.fragment.EbookFragment;
import com.discovertodo.phone.android.model.TagClassHtml;
import com.discovertodo.phone.android.util.JsonUtils;
import com.discovertodo.phone.android.util.ProgressHUD;
import com.discovertodo.phone.android.util.StoreData;

import org.json.JSONArray;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class EpubWebView extends WebView {
    private int totalPageCount;
    private int currentPage;
    private int topP = 0;
    private float currentState;
    private EbookFragment ebookFragment;
    private float textSize = (float) 1.0, MAX_TEXT_SIZE = (float) 2.0,
            MIN_TEXT_SIZE = (float) 0.6;
    public static Boolean isScroll = false;
    public Context cont;
    private String jsonConfig;
    private StoreData storeData;

    @SuppressLint("SetJavaScriptEnabled")
    public EpubWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalFadingEdgeEnabled(true);
        cont = context;
        jsonConfig = JsonUtils.getJson(cont);
        initWebView();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(false);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setVerticalScrollBarEnabled(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
    }


    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
//        addJQueryJS();
//        setWebViewClient(new MyWebClient(mContext));
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public static interface Callback {
        public void update();
    }


    Callback call;



    public String getJsonConfig() {
        return jsonConfig;
    }

    public void initWebView() {
        this.totalPageCount = 200;
        this.currentPage = 0;
        this.currentState = 0;
        storeData=new StoreData(cont);



    }

    public void goToPage(int pageNumber) {
        this.currentPage = pageNumber;
        this.updateCurrentState();
        if (!isScroll)
            this.scrollTo((currentPage) * this.getWidth(), 0);
        else
            this.scrollTo(0, (currentPage) * this.getHeight());
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public EbookFragment getEbookFragment() {
        return ebookFragment;
    }

    public void setEbookFragment(EbookFragment ebookFragment) {
        this.ebookFragment = ebookFragment;
    }

    public float getCurrentState() {
        return this.currentState;
    }

    public void updateCurrentState() {
        this.currentState = (float) (currentPage + 0.5) / (totalPageCount);
    }

    public void updateSeekBar() {
        if (EpubWebView.isScroll) {
            this.ebookFragment.getSeekBarVertical().setProgress(currentPage);
        } else
            this.ebookFragment.getSeekBar().setProgress(currentPage);
    }

    public boolean setTextSizeIncrease() {
        textSize += 0.2;
        storeData.setTextSize("textSize",textSize);
        String textSize1 = "document.body.style.fontSize = \"" + textSize
                + "em\"";
        loadJavascript(this, "javascript:" + textSize1);
        ProgressHUD.show(cont, "", false);
        final String[] listFont = getResources().getStringArray(
                R.array.font);
        StoreData data = new StoreData(cont);
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
        loadJavascript(this, "javascript:" + strAddStyle);
        setfont();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCountPage();
            }
        }, 200);
        if
                (textSize > MAX_TEXT_SIZE)
            return false;
        return true;
    }

    public boolean setTextSizeReduction() {
        textSize -= 0.2;
        storeData.setTextSize("size",textSize);
        String textSize1 = "document.body.style.fontSize = \"" + textSize
                + "em\"";
        loadJavascript(this, "javascript:" + textSize1);
        ProgressHUD.show(cont, "", false);
        final String[] listFont = getResources().getStringArray(
                R.array.font);
        StoreData data = new StoreData(cont);
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
        loadJavascript(this, "javascript:" + strAddStyle);
        setfont();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCountPage();
            }
        }, 200);
        if (textSize < MIN_TEXT_SIZE)
            return false;
        return true;

    }

    public void setClickWeb() {
        try {
            WebView.HitTestResult result = EpubWebView.this.getHitTestResult();
            if (result.getType() == HitTestResult.IMAGE_TYPE) {
                Intent intent = new Intent(ebookFragment.getActivity(), ShowImageActivity.class);
                intent.putExtra("image", String.valueOf(result.getExtra()));
                ebookFragment.getActivity().startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    public void setFontWeight() {
        String strSetWeight = "var colorPs = document.getElementsByTagName(\"*\");"
                + "var colorLis = document.getElementsByTagName('Li');"
                + "var colorSpans = document.getElementsByTagName('span');" +
                "for(var i = 0; i < colorPs.length; i++){var e=colorPs[i];e.style.fontWeight='normal';e.style.lineHeight='1.5';e.style.textDecoration='none';}"
                + "for(var i = 0; i < colorLis.length; i++){var a=colorPs[i];a.style.fontWeight='normal';a.style.lineHeight='1.5';e.style.textDecoration='none';}"
                + "for(var i = 0; i < colorSpans.length; i++){var c=colorSpans[i];c.style.fontWeight='normal';c.style.lineHeight='1.5';e.style.textDecoration='none';}";
        loadJavascript(this, strSetWeight);
        ArrayList<TagClassHtml> textbolds = new ArrayList<>();
        JSONArray lists = JsonUtils.getListJsonOject(JsonUtils.getJson(cont), "TextBolds");
        textbolds = JsonUtils.getListObject(lists);
        if (textbolds!=null) {
            for (TagClassHtml tagClassHtml : textbolds) {
                String nameclass = tagClassHtml.getNameClass();
                String position = String.valueOf(tagClassHtml.getPosition());
                String srtSetBold = "var i=document.getElementsByClassName('" + nameclass + "')[" + position + "];" +
                        "i.style.fontWeight='bold';";
                loadJavascript(this, "javascript:" + srtSetBold);
            }
        }
        String setTextNormal="var setTextNormal=document.getElementsByClassName('s3 S6')[0];setTextNormal.style.fontWeight='normal';"+
                             "var e=document.getElementsByClassName('s5 s6')[16];e.style.fontWeight='normal';";
        loadJavascript(this, setTextNormal);
        String setbold="var bold = document.getElementsByClassName('c1');"
        + "for(var i = 0; i < bold.length; i++){var a=bold[i];a.style.fontWeight='bold';}";
        loadJavascript(this, setbold);


    }

    public void changeTextColorToGray() {
        String setGrayColorString = "var colorPs = document.getElementsByTagName(\"*\");"
                + "var colorSpans = document.getElementsByTagName('span');"
                + "var colorLis = document.getElementsByTagName('Li');" +
                "for(var i = 0; i < colorPs.length; i++){colorPs[i].style.color='gray';}" +
                "for(var i = 0; i < colorPs.length; i++){colorSpans[i].style.color='gray';}"
                + "for(var i = 0; i < colorLis.length; i++){colorLis[i].style.color='gray';}";
        loadJavascript(EpubWebView.this, setGrayColorString);
    }

    public void changeTextColorToBlack() {
        String setBackColorString = "var colorPs = document.getElementsByTagName(\"*\");"
                + "var colorSpans = document.getElementsByTagName('span');"
                + "var colorLis = document.getElementsByTagName('Li');" +
                "for(var i = 0; i < colorPs.length; i++){colorPs[i].style.color='black';}" +
                "for(var i = 0; i < colorPs.length; i++){colorSpans[i].style.color='black';}"
                + "for(var i = 0; i < colorLis.length; i++){colorLis[i].style.color='black';}";
        loadJavascript(this, setBackColorString);
        float textSize=getTextSize()-0.05f;
        String setFontGray="var al=document.getElementsByClassName('s3');"+
        "for(var i = 0; i < al.length; i++){"
                +"if(i==25){"
                +"var e=al[i];e.style.color='#515151';"
                + "e.style.fontSize='"+(textSize+0.2f)+"em';"
                +"}else{"
                     +"var e=al[i];e.style.color='#515151';" +
                "e.style.fontSize='"+textSize+"em';}}";//gray
        loadJavascript(this, setFontGray);

            ArrayList<TagClassHtml> textGrays = new ArrayList<>();
            JSONArray list = JsonUtils.getListJsonOject(JsonUtils.getJson(cont), "textGrays");
            textGrays = JsonUtils.getListObject(list);
            if(textGrays!=null){
                for (TagClassHtml tag : textGrays) {
                    String nameClass = tag.getNameClass();
                    String postion = String.valueOf(tag.getPosition());
                    String setAlin = "var mLeft=document.getElementsByClassName('"+nameClass+"')["+postion+"];"+
                            "mLeft.style.color='#515151';";
                    EpubWebView.loadJavascript(this, "javascript:" + setAlin);

                }
            }


    }

    public void changeFont(String strNameFont, int id) {
        ProgressHUD.show(cont, "", false);
        String strRunFont = "allp = document.getElementsByTagName('p');"
                + "for (i = 0; i < allp.length; i++) { tagp = allp[i]; tagp.style.fontFamily = 'FontName"
                + id + "';} ";
        final String strAddStyle = "var newStyle = document.createElement( 'style' );"
                + "newStyle.appendChild( document.createTextNode( "
                + "\"@font-face { "
                + "font-family: 'FontName"
                + id
                + "'; "
                + "src: url('"
                + strNameFont
                + "') format('opentype'); "
                + "} \") );"
                + " document.head.appendChild( newStyle ); "
                + strRunFont;
        this.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                loadJavascript(EpubWebView.this, "javascript:" + strAddStyle);
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                updateCountPage();
            }
        }, 200);
    }

    public void updateCountPage() {
//        if (EbookFragment.type_light == 3) {
//            changeTextColorToGray();
//        } else {
//            changeTextColorToBlack();
//        }

        String strGetTop = getStringGetTop();
        setFontWeight();
        setfont();

        loadJavascript(this, "javascript:( function () { var resultSrc = document.documentElement.scrollWidth; window.HTMLOUT.scrollWidth(resultSrc); } ) ()");
        loadJavascript(this, "javascript:( function () { var resultSrc = document.documentElement.scrollHeight; window.HTMLOUT.scrollHeight(resultSrc); } ) ()");
        loadJavascript(this, "javascript:( function () { var resultSrc2 = document.getElementsByTagName('tbody')[0].parentElement.offsetTop; window.HTMLOUT.topTable(resultSrc2); } ) ()");
        loadJavascript(this, "javascript:( function () { var resultSrc3 = document.getElementsByTagName('tbody')[0].parentElement.offsetHeight; window.HTMLOUT.heightTable(resultSrc3); } ) ()");

        final String strArrTagP = "var arrTagB = [];"
                + " var arr = document.getElementsByTagName(\"P\");"
                + "for(i=0;i<arr.length;i++){  arrTagB.push(arr[i].offsetTop)}";

        final String strArrTagP1 = "var arrTagB1 = [];"
                + " var arr1 = document.getElementsByTagName(\"P\");"
                + "for(i=0;i<arr1.length;i++){  arrTagB1.push(arr1[i].offsetHeight) }";

        final String finalStrGetTop = strGetTop;
        new CountDownTimer(1000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                loadJavascript(EpubWebView.this, "javascript:( function () { " + finalStrGetTop
                        + " window.HTMLOUT.getTop(heighArray); } ) ()");
                loadJavascript(EpubWebView.this, "javascript:( function () { " + strArrTagP
                        + " window.HTMLOUT.getTopTagP(arrTagB); } ) ()");
                loadJavascript(EpubWebView.this, "javascript:( function () { " + strArrTagP1
                        + " window.HTMLOUT.getHeightP(arrTagB1); } ) ()");
                topP = currentPage
                        * (int) (getHeight() / cont.getResources().getDisplayMetrics().density);
            }
        }.start();
    }

    private void setfont() {
        String srtSetfont = "var i=document.getElementsByClassName('s3 s5')[0];"
                +"var b=document.getElementsByClassName('s3 s14')[0];"
                +"var center1=document.getElementsByClassName('s3 s6')[0]"
                +"var center2=document.getElementsByClassName('s3 s7')[0]"
                +  "i.style.fontSize='0.2em';"+
                "b.style.fontSize='0.2em';"+
                "center1.style.textAlign='center';"+
                "center2.style.textAlign='center';";
        loadJavascript(this, "javascript:" + srtSetfont);


    }

    private String getStringGetTop() {
        ArrayList<TagClassHtml> menus = new ArrayList<>();
        JSONArray lists = JsonUtils.getListJsonOject(jsonConfig, "TagClassHtmls");
        menus = JsonUtils.getListObject(lists);
        String result = "var heighArray = [];";
        if(menus!=null){
            if (menus.size() >= 1) {
                for (int i = 1; i <= menus.size(); i++) {
                    String nameclass = menus.get(i - 1).getNameClass();
                    String positon = String.valueOf(menus.get(i - 1).getPosition());
                    String top = "heighArray.push(document.getElementsByClassName('" + nameclass + "')[" + positon + "].offsetTop + 5);";
                    result = result.concat(top);

                }
            }
        }
        return result;
    }

    public void scrollHorizontal() {
        isScroll = false;
        new HorizontalPagination().canScrollHorizontalPager(this);
    }

    public void scrollVertical() {
        isScroll = true;
        new VerticalPagination().canScrollVerticalPager(this);
    }

    public void setColorTextSearch(int intP, int intPos, String str) {
        String text = "function allIndexOf(str, toSearch) {"
                + "var indices = [];"
                + " for(var pos = str.indexOf(toSearch); pos !== -1; pos = str.indexOf(toSearch, pos + 1)) {"
                + "indices.push(pos);"
                + "}"
                + "return indices;"
                + "}"
                + "function setColorText(pNumber, text, index){"
                + "var obj = document.getElementsByTagName('p')[pNumber];"
                + "var content = obj.innerText;"
                + "var keyWordLowcase = text.toLowerCase();"
                + "var contentLowcase = obj.innerText.toLowerCase();"
                + "var lengthContent = content.length;"
                + "var lengthKeyWord = text.length;"
                + "var countNumberIndex = allIndexOf(contentLowcase, keyWordLowcase);"
                + "if(countNumberIndex < 0){"
                + "console.log('Not found');"
                + "return false;}"
                + "if(index >= countNumberIndex.length){"
                + "console.log('Invalid index');"
                + "return false;}"
                + "var indexText = countNumberIndex[index];"
                + "if(indexText >= 0){"
                + "var iCount = 0;"
                + "var textArray = [];"
                + "textArray.push(content.substr(iCount, indexText));"
                + "iCount = iCount + indexText;"
                + "textArray.push(\"<span class='highlighted'><b>\" +  content.substr(iCount, lengthKeyWord) + \"</b></span>\");"
                + "iCount = iCount + lengthKeyWord;"
                + "textArray.push(content.substr(iCount,lengthContent));"
                + "textTemp = '';"
                + "if(textArray){"
                + "for(i = 0; i < textArray.length; i++ ){"
                + "textTemp += textArray[i];"
                + "}}"
                + "obj.innerHTML = textTemp;"
                + "obj.getElementsByClassName('highlighted')[0].setAttribute(\"style\",\"background-color: yellow;font-family: Impact; font-size:100%;\");}}"
                + "setColorText(" + intP + ",'"
                + str.replace("(", "（").replace(")", "）") + "', " + intPos
                + ");";
        loadJavascript(this, "javascript:" + text);
    }

    public void removeColorTextSearch(int intP, int intPos, String str) {
        String text = "function removeColorText(pNumber, text, index){"
                + " var obj = document.getElementsByTagName('p')[pNumber];"
                + " var str = obj.innerText;" + " obj.innerHTML = str;}"
                + "removeColorText(" + intP + ",'" + str + "', " + intPos
                + ");";
        loadJavascript(this, "javascript:" + text);
        this.updateCountPage();
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        if (isScroll) {
            topP = (int) (top / cont.getResources().getDisplayMetrics().density);
            this.setCurrentPage((int) (top / getHeight()));
            this.updateSeekBar();
            this.updateCurrentState();
        }

        super.onScrollChanged(left, top, oldLeft, oldTop);
    }

    public void updateView(Callback _call) {
        call = _call;
    }

    public void updateView() {
        if (call != null)
            call.update();
    }

    public void dismisDialog() {
        try {
            ProgressHUD.mDismiss();
        } catch (Exception e) {
        }
    }

    public int getTopP() {
        return topP;
    }

    public float getTextSize() {
        return textSize;
    }

    public static void loadJavascript(WebView webView, String javascript) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            /**
             In KitKat+ you should use the evaluateJavascript method
             **/
            webView.evaluateJavascript(javascript, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if (reader.peek() != JsonToken.NULL) {
                            if (reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if (msg != null) {
                                    Log.e(EpubWebView.class.getSimpleName(), msg);
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e(EpubWebView.class.getSimpleName(), "IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            });
        } else {
            /**
             * For pre-KitKat+ you should use loadUrl("javascript:<JS Code Here>");
             * To then call back to Java you would need to use addJavascriptInterface()
             * and have your JS call the interface
             **/
            webView.loadUrl(javascript);
        }
    }
}

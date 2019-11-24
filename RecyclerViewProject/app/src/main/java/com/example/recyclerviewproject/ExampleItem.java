package com.example.recyclerviewproject;

public class ExampleItem {
    private int ImageResource;
    private String Text1;
    private String Text2;

    public ExampleItem(int mImageResource, String text1, String text2){
        ImageResource = mImageResource;
        Text1 = text1;
        Text2 = text2;
    }

    public int getImageResource(){
        return ImageResource;
    }
    public String getText1(){
        return Text1;
    }
    public String getText2(){
        return Text2;
    }

}

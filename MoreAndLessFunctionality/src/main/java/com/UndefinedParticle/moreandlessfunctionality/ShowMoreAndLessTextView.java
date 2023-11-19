package com.UndefinedParticle.moreandlessfunctionality;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;

import java.util.Collections;

public class ShowMoreAndLessTextView extends ClickableSpan {

    private final Context context;

    public ShowMoreAndLessTextView(Context context) {
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setUnderlineText(false);
        textPaint.setColor(Color.parseColor("#2196F3"));
    }

    @Override
    public void onClick(View widget) {

    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("deprecation")
    public void setResizableText(TextView textView, String fullText, int maxLines, boolean viewMore, ApplyExtraHighlights applyExtraHighlights) {
        try {
            if (textView == null || fullText == null) {
                return;
            }
            int width = textView.getWidth();
            if (width <= 0) {
                textView.post(() -> setResizableText(textView, fullText, maxLines, viewMore, applyExtraHighlights));
                return;
            }
            textView.setMovementMethod(LinkMovementMethod.getInstance());

            String adjustedText = fullText.replace("\r\n", "\n");
            StaticLayout textLayout = new StaticLayout(
                    adjustedText,
                    textView.getPaint(),
                    width - textView.getPaddingLeft() - textView.getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL,
                    textView.getLineSpacingMultiplier(),
                    textView.getLineSpacingExtra(),
                    textView.getIncludeFontPadding()
            );

            if (textLayout.getLineCount() <= maxLines || adjustedText.isEmpty()) {
                String htmlText = adjustedText.replace("\n", "<br/>");
                textView.setText(addClickablePartTextResizable(
                        textView, fullText, maxLines, HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                        null, viewMore, applyExtraHighlights
                ));
                return;
            }

            int charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1);
            String suffixText = viewMore ? "...more" : "...less";

            int charactersToTake = charactersAtLineEnd - suffixText.length() / 2;

            if (charactersToTake <= 0) {
                String htmlText = adjustedText.replace("\n", "<br/>");
                textView.setText(addClickablePartTextResizable(
                        textView, fullText, maxLines, HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                        null, viewMore, applyExtraHighlights
                ));
                return;
            }

            if (!viewMore) {
                String htmlText = adjustedText.replace("\n", "<br/>");
                textView.setText(addClickablePartTextResizable(
                        textView, fullText, maxLines, HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                        suffixText, viewMore, applyExtraHighlights
                ));
                return;
            }

            String linedText = adjustedText;


            if (linedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1)).contains("\n")) {
                //int charactersPerLine = textLayout.getLineEnd(0) / (int) (textLayout.getLineWidth(0) / textLayout.getPaint().getTextSize());
                float lineWidth = textLayout.getLineWidth(0);
                float textSize = textLayout.getPaint().getTextSize();
                int charactersPerLine = (textSize != 0) ? (int) (textLayout.getLineEnd(0) / (lineWidth / textSize)) : 1;

                String lineOfSpaces = String.join("", Collections.nCopies(charactersPerLine, "\u00A0"));
                charactersToTake += lineOfSpaces.length() - 1;

                //Here we are replacing newline characters in a specific line (If any error occurs).
            /*String modifiedLine = linedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
                    .replace("\n", lineOfSpaces);
            linedText = linedText.substring(0, textLayout.getLineStart(maxLines - 1))
                    + modifiedLine
                    + linedText.substring(textLayout.getLineEnd(maxLines - 1));*/
                linedText = linedText.substring(0, textLayout.getLineStart(maxLines - 1))
                        + linedText.substring(textLayout.getLineStart(maxLines - 1), textLayout.getLineEnd(maxLines - 1))
                        .replace("\n", lineOfSpaces)
                        + linedText.substring(textLayout.getLineEnd(maxLines - 1));
            }

            String shortenedString = linedText.substring(0, charactersToTake);
            String shortenedStringWithSuffix = shortenedString + suffixText;

            StaticLayout shortenedStringWithSuffixLayout = new StaticLayout(
                    shortenedStringWithSuffix,
                    textView.getPaint(),
                    width - textView.getPaddingLeft() - textView.getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL,
                    textView.getLineSpacingMultiplier(),
                    textView.getLineSpacingExtra(),
                    textView.getIncludeFontPadding()
            );

            int modifier;
            if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length()) {
                modifier = 1;
                charactersToTake--;
            } else {
                modifier = -1;
            }

            charactersToTake += modifier;
            String baseString = linedText.substring(0, charactersToTake);
            String appended = baseString + suffixText;
            StaticLayout newLayout = new StaticLayout(
                    appended,
                    textView.getPaint(),
                    width - textView.getPaddingLeft() - textView.getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL,
                    textView.getLineSpacingMultiplier(),
                    textView.getLineSpacingExtra(),
                    textView.getIncludeFontPadding()
            );
            do {
                charactersToTake += modifier;
                baseString = linedText.substring(0, charactersToTake);
                appended = baseString + suffixText;


                newLayout = new StaticLayout(
                        appended,
                        textView.getPaint(),
                        width - textView.getPaddingLeft() - textView.getPaddingRight(),
                        Layout.Alignment.ALIGN_NORMAL,
                        textView.getLineSpacingMultiplier(),
                        textView.getLineSpacingExtra(),
                        textView.getIncludeFontPadding()
                );

            } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length()) ||
                    (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length()));

            if (modifier > 0) {
                charactersToTake--;
            }

            String htmlText = linedText.substring(0, charactersToTake).replace("\n", "<br/>");
            textView.setText(addClickablePartTextResizable(
                    textView, fullText, maxLines, HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT),
                    suffixText, viewMore, applyExtraHighlights
            ));
        } catch (StringIndexOutOfBoundsException e) {
            Log.e("MoreAndLessFunctionality", "StringIndexOutOfBoundsException in setResizableText", e);
            Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MoreAndLessFunctionality", "Generic Exception in setResizableText", e);
            Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show();
        }

    }


    private Spannable addClickablePartTextResizable(TextView textView, String fullText, int maxLines, Spanned shortenedText, String clickableText, boolean viewMore, ApplyExtraHighlights applyExtraHighlights) {

        SpannableStringBuilder builder = new SpannableStringBuilder(shortenedText);

        if (clickableText != null) {
            builder.append(clickableText);

            //int startIndexOffset = viewMore ? 0 : 0;
            int startIndexOffset = 0;
            builder.setSpan(new ShowMoreAndLessTextView(textView.getContext()) {
                                @Override
                                public void onClick(View widget) {
                                    if (viewMore) {
                                        setResizableText(textView, fullText, maxLines, false, applyExtraHighlights);
                                    } else {
                                        setResizableText(textView, fullText, maxLines, true, applyExtraHighlights);
                                    }
                                }
                            }, builder.length() - clickableText.length() + startIndexOffset,
                    builder.length(), 0);
        }

        if (applyExtraHighlights != null) {
            return applyExtraHighlights.apply(builder);
        }

        return builder;
    }


    @FunctionalInterface
    public interface ApplyExtraHighlights {
        Spannable apply(Spannable spannable);
    }

}
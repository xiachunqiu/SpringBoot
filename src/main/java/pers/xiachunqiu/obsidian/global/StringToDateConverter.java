package pers.xiachunqiu.obsidian.global;

import pers.xiachunqiu.obsidian.util.StringUtils;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StringToDateConverter implements Converter<String, Date> {
    @Getter
    private List<String> dateFormatList = new ArrayList<>();
    private final ThreadLocal<List<String>> formatListLocal = new ThreadLocal<>();

    private void initFormat() {
        if (this.formatListLocal.get() == null) {
            this.formatListLocal.set(this.getDateFormatList());
        }
        List<String> dateFormatListTemp = this.formatListLocal.get();
        dateFormatListTemp.add("yyyy-MM-dd HH:mm:ss");
        dateFormatListTemp.add("yyyy-MM-dd");
        dateFormatListTemp.add("yyyy/MM/dd");
        this.formatListLocal.set(dateFormatListTemp);
    }

    @SuppressWarnings("NullableProblems")
    public Date convert(String dateString) {
        if (StringUtils.isNull(dateString)) {
            return null;
        } else {
            this.initFormat();
            boolean mark = false;
            Date date = null;
            StringBuilder sBuffer = new StringBuilder();
            int count = 0;
            List<String> dateFormatListTemp = this.formatListLocal.get();
            for (String dateFormatString : dateFormatListTemp) {
                ++count;
                sBuffer.append(dateFormatString);
                if (count < this.dateFormatList.size()) {
                    sBuffer.append(",");
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString, Locale.CHINA);
                try {
                    date = dateFormat.parse(dateString);
                    mark = true;
                    break;
                } catch (ParseException var11) {
                    var11.printStackTrace();
                }
            }
            if (!mark) {
                throw new IllegalArgumentException(String.format("类型转换失败，需要格式[" + sBuffer.toString() + "]，但格式是[%s]", dateString));
            } else {
                if (this.formatListLocal.get() == null) {
                    this.formatListLocal.remove();
                }
                return date;
            }
        }
    }
}
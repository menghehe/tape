package site.imcu.tape.mapper;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author MengHe
 * @date 2020/4/13 15:49
 */
@Data
public class MainTable {
    private String 凭证编号;
    private String 主表编号;
    private String 日期;
    private String 过账期间;
    private String 单据日期;
    private String 审批类型;
    private String 提交人;
    private String 备注;
    private String 子公司名称;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainTable mainTable = (MainTable) o;
        return Objects.equals(凭证编号, mainTable.凭证编号) &&
                Objects.equals(主表编号, mainTable.主表编号) &&
                Objects.equals(日期, mainTable.日期) &&
                Objects.equals(过账期间, mainTable.过账期间) &&
                Objects.equals(单据日期, mainTable.单据日期) &&
                Objects.equals(审批类型, mainTable.审批类型) &&
                Objects.equals(提交人, mainTable.提交人) &&
                Objects.equals(备注, mainTable.备注) &&
                Objects.equals(子公司名称, mainTable.子公司名称);
    }

    @Override
    public int hashCode() {
        return Objects.hash(凭证编号, 主表编号, 日期, 过账期间, 单据日期, 审批类型, 提交人, 备注, 子公司名称);
    }
}

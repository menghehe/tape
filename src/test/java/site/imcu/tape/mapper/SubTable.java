package site.imcu.tape.mapper;

import lombok.Data;

/**
 * @author MengHe
 * @date 2020/4/13 16:29
 */
@Data
public class SubTable {
    private String 主表编号;
    private String 货币;
    private String 汇率;
    private String 日记账父项;
    private String 行号;
    private String 科目编码;
    private String 科目名称;
    private String 本币借记;
    private String 本币贷记;
    private String 原币金额;
    private String 外币借记;
    private String 外币贷记;
    private String 客户;
    private String 供应商;
    private String 部门;
    private String 员工;
    private String 展会项目辅助核算;
    private String 摊销维度;
    private String 国度;
    private String 现金流量项目;
    private String 摘要;
    private String 银行账号;
    private String 库存现金币别;
    private String 内部标识;
}

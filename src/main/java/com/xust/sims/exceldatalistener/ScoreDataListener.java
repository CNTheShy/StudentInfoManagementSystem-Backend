package com.xust.sims.exceldatalistener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.xust.sims.dto.ScoreExcelData;
import com.xust.sims.service.ScoreInfoService;
import com.xust.sims.service.ScoreInfoServiceImpl;
import com.xust.sims.utils.ExcelValidateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ScoreDataListener extends AnalysisEventListener<ScoreExcelData> {
    private static final int BATCH_COUNT = 20;
    private final List<ScoreExcelData> list = new ArrayList<>();
    private ScoreInfoService scoreInfoService;

    public ScoreDataListener(ScoreInfoService scoreInfoService) {
        this.scoreInfoService = scoreInfoService;
    }

    /**
     * 每一条数据解析都会调用该方法
     * @param scoreExcelData 一行的value
     * @param analysisContext
     */
    @Override
    public void invoke(ScoreExcelData scoreExcelData, AnalysisContext analysisContext) {
        log.info("Parse to an excel data：{}", JSON.toJSONString(scoreExcelData));
        String errorMessage = null;
        try {
            errorMessage = ExcelValidateHelper.validateEntity(scoreExcelData);
        } catch (NoSuchFieldException e) {
            errorMessage = "Parsing data error";
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(errorMessage)) {
            list.add(scoreExcelData);
        } else {
            scoreExcelData.setDescription(errorMessage);
            ((ScoreInfoServiceImpl) scoreInfoService).getErrorList().add(scoreExcelData);
        }
        //达到BATCH_COUNT，需要存储一次数据库，防止几万条数据在内存中，出现OOM
        if (list.size() >= BATCH_COUNT) {
            try {
                scoreInfoService.saveExcelData(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.clear();
        }
    }

    /**
     * 在转换异常，获取其他异常下会调用该接口。抛出异常则停止读取。如果这里不抛出异常，则继续读取下一行
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("Parsing failed, but continue parsing the next line:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("Row {}, column {} resolves the exception", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
        }
    }

    /**
     * 所有数据都被解析完成，会调用该方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            if (list.size() > 0) {
                scoreInfoService.saveExcelData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("All data has been parsed!");
        list.clear();
    }
}

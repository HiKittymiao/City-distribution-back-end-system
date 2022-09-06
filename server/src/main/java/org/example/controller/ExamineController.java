package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.example.common.R;
import org.example.pojo.Examine;
import org.example.service.IExamineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mcy
 * @since 2022-08-12
 */
@RestController
@RequestMapping("/examine")
public class ExamineController {
    @Autowired
    private IExamineService examineService;

    @ApiOperation(value = "添加审核信息(骑手注册)")
    @PostMapping("/add")
    public R addExamine(@RequestBody Examine examine){
        examine.setCreateTime(LocalDateTime.now());
        examine.setUpdateTime(LocalDateTime.now());

        Examine phone = examineService.getOne(new QueryWrapper<Examine>().eq("phone", examine.getPhone()).ne("status",1));
        if (phone ==null){
            if (examineService.save(examine)){
                return R.success("添加成功");
            }
        }
        return R.error("添加失败,请审核信息是否填写正确");
    }

}

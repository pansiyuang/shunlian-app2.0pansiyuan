package com.shunlian.app.view;

import com.shunlian.app.bean.ExperienceEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public interface IExperienceView extends IView {

    void getExperienceList(List<ExperienceEntity.Experience> experienceList, int page, int totalPage);

    void praiseExperience();

    void refreshFinish();
}

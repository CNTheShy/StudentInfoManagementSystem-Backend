package com.xust.sims.service;

import com.xust.sims.entity.Menu;

import java.util.List;


public interface MenuService {
    /**
     * 根据当前的 用户身份，来获取菜单
     * @param status token
     * @return 菜单
     */
    List<Menu> initMenus(Integer status);
}

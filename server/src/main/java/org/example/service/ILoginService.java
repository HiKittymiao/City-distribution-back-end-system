package org.example.service;

import org.example.common.R;

/**
 * ClassName:ILoginService
 * Package:org.example.service
 * Description:
 *
 * @Date:2022/8/6 21:03
 * @Author:cbb
 */
public interface ILoginService {
    R login(String username, String password);
}

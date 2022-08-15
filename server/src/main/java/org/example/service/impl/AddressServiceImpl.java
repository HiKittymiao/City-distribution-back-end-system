package org.example.service.impl;

import org.example.pojo.Address;
import org.example.mapper.AddressMapper;
import org.example.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mcy
 * @since 2022-08-14
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}

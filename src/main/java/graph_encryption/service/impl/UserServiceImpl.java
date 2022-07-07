package graph_encryption.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.domain.client.UserInfoMapper;
import graph_encryption.domain.model.*;
import graph_encryption.model.UserLoginInfo;
import graph_encryption.model.UserText;
import graph_encryption.service.UserService;
import graph_encryption.util.CipherHelper;
import graph_encryption.util.HttpUtils;
import graph_encryption.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.jws.soap.SOAPBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    private final String RESTAURANT_DETAIL_API = "https://api.yelp.com/v3/businesses/";

    @Override
    public JSONObject getUserInfo(int id) {
        UserInfo userInfos = userInfoMapper.selectByPrimaryKey(id);
        return (JSONObject) JSONObject.toJSON(userInfos);
    }

    @Override
    public void updateUserInfo(JSONObject user) {
        UserInfo userInfo = user.toJavaObject(UserInfo.class);
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userInfo.getUserId());
        userInfoMapper.updateByExampleSelective(userInfo, example);
    }

    @Override
    public void deleteUser(int id) {
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(id);
        userInfoMapper.deleteByExample(example);
    }

    @Override
    public void createUser(JSONObject user) throws DuplicateKeyException {
        UserInfo userInfo = user.toJavaObject(UserInfo.class);
        String passWdHash = CipherHelper.getSHA256(userInfo.getPassword());
        userInfo.setPassword(passWdHash);
        userInfoMapper.insert(userInfo);
    }

    @Override
    public Result<JSONObject> login(JSONObject user) {
        UserLoginInfo userLoginInfo = user.toJavaObject(UserLoginInfo.class);
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(userLoginInfo.getEmail());
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        if (userInfos == null || userInfos.size() == 0) {
            return new Result("This email does not register.", 0 );
        } else {
            String passWdHash = CipherHelper.getSHA256(userLoginInfo.getPassword());
            if (userInfos.get(0).getPassword().equals(passWdHash)) {
                UserInfo userInfo = userInfos.get(0);
                UserText userText = new UserText();
                userText.setEmail(userInfo.getEmail());
                userText.setToken(JwtUtils.createToken(userLoginInfo));
                userText.setId(userInfo.getUserId());
                return new Result(userText,1);
            } else {
                return new Result("The password is not correct.", 0);
            }
        }
    }


}

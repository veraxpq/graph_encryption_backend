package graph_encryption.service.impl;

import com.alibaba.fastjson.JSONObject;
import graph_encryption.common.Result;
import graph_encryption.domain.client.UserInfoMapper;
import graph_encryption.domain.model.UserInfo;
import graph_encryption.domain.model.UserInfoExample;
import graph_encryption.model.UserLoginInfo;
import graph_encryption.model.UserText;
import graph_encryption.service.UserService;
import graph_encryption.util.CipherHelper;
import graph_encryption.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represents the implementation of the UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<JSONObject> getUserInfo(int id) {
        UserInfo userInfos = userInfoMapper.selectByPrimaryKey(id);
        if (userInfos == null) {
            JSONObject error = new JSONObject();
            error.put("error", "Illegal userId");
            return new Result<>(error, 0);
        }
        userInfos.setPassword(null);
        JSONObject job = (JSONObject) JSONObject.toJSON(userInfos);
        return new Result<>(job, 1);
    }

    @Override
    public Result updateUserInfo(JSONObject user) {
        UserInfo userInfo = user.toJavaObject(UserInfo.class);
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userInfo.getUserId());
        userInfoMapper.updateByExampleSelective(userInfo, example);
        return new Result("", 1);
    }

    @Override
    public Result createUser(JSONObject user) {
        UserInfo userInfo = user.toJavaObject(UserInfo.class);
        String passWdHash = CipherHelper.getSHA256(userInfo.getPassword());
        userInfo.setPassword(passWdHash);
        try {
            userInfoMapper.insert(userInfo);
        } catch (DuplicateKeyException e) {
            JSONObject error = new JSONObject();
            error.put("error", "This email already registered, please login.");
            return new Result(error, 0);
        }
        return new Result("", 1);
    }

    @Override
    public Result<JSONObject> login(JSONObject user) {

        System.out.println("@tom hihiih");
        UserLoginInfo userLoginInfo = user.toJavaObject(UserLoginInfo.class);
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(userLoginInfo.getEmail());
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        if (userInfos == null || userInfos.size() == 0) {
            return new Result("This email does not register.", 0);
        } else {
            String passWdHash = CipherHelper.getSHA256(userLoginInfo.getPassword());
            if (userInfos.get(0).getPassword().equals(passWdHash)) {
                UserInfo userInfo = userInfos.get(0);
                UserText userText = new UserText();
                userText.setEmail(userInfo.getEmail());
                userText.setToken(JwtUtils.createToken(userLoginInfo));
                userText.setId(userInfo.getUserId());
                return new Result(userText, 1);
            } else {
                return new Result("The password is not correct.", 0);
            }
        }
    }


}

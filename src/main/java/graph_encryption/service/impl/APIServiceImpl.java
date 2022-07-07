//package graph_encryption.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import graph_encryption.domain.client.ReviewInfoMapper;
//import graph_encryption.domain.model.ReviewInfo;
//import graph_encryption.domain.model.ReviewInfoExample;
//import graph_encryption.model.ReviewReturnInfo;
//import graph_encryption.service.APIService;
//import graph_encryption.util.HttpUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class APIServiceImpl implements APIService {
//
//    private final String LOCATION_API = "https://api.yelp.com/v3/businesses/search";
//
//    private final String RESTAURANT_DETAIL_API = "https://api.yelp.com/v3/businesses/";
//
//    private final String REVIEWS_API = "https://api.yelp.com/v3/businesses/";
//
//    @Autowired
//    private ReviewInfoMapper reviewInfoMapper;
//
//    @Override
//    public JSONObject searchRestaurantsByLocation(String cityName) {
//        Map<String, String> map = new HashMap<>();
//        map.put("term", "restaurants");
//        map.put("location", cityName);
//        String result = HttpUtils.getRequest(LOCATION_API, map);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        return resultJson;
//    }
//
//    @Override
//    public JSONObject searchRestaurantsByLocationAndTerm(JSONObject obj) {
//        String location = obj.getString("location");
//        JSONArray term = obj.getJSONArray("term");
//        Map<String, String> map = new HashMap<>();
//        map.put("term", term.toJSONString());
//        map.put("location", location);
//        String result = HttpUtils.getRequest(LOCATION_API, map);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        return resultJson;
//    }
//
//    @Override
//    public JSONObject getRestaurantInfoById(String id) {
//        String url = RESTAURANT_DETAIL_API + id;
//        String result = HttpUtils.getRequest(url, null);
//        return (JSONObject) JSONObject.parse(result);
//    }
//
//    @Override
//    public JSONArray getReviewsByRestaurantId(String id) {
//        JSONArray array = new JSONArray();
//        ReviewInfoExample example = new ReviewInfoExample();
//        ReviewInfoExample.Criteria criteria = example.createCriteria();
//        criteria.andRestaurantIdEqualTo(id);
//        List<ReviewInfo> reviewInfos = reviewInfoMapper.selectByExample(example);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        for (ReviewInfo info : reviewInfos) {
//            ReviewReturnInfo resultInfo = new ReviewReturnInfo();
//            resultInfo.setRestaurantId(info.getRestaurantId());
//            resultInfo.setRestaurantName(info.getRestaurantName());
//            resultInfo.setId(info.getId());
//            resultInfo.setRating(info.getRating());
//            resultInfo.setText(info.getText());
//            resultInfo.setUser((JSONObject) JSONObject.parse(info.getUser()));
//            String createTime = simpleDateFormat.format(info.getTimeCreated());
//            resultInfo.setTime_created(createTime);
//            resultInfo.setUserId(info.getUserId());
//            array.add(resultInfo);
//        }
//        String url = REVIEWS_API + id + "/reviews";
//        String result = HttpUtils.getRequest(url, null);
//        JSONObject apiResult = (JSONObject) JSONObject.parse(result);
//        if (apiResult.getString("error") == null) {
//            JSONArray apiArray = apiResult.getJSONArray("reviews");
//            for (int i = 0; i < apiArray.size(); i++) {
//                array.add(apiArray.get(i));
//            }
//        }
//        return array;
//    }
//}

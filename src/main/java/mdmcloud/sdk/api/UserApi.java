package mdmcloud.sdk.api;

import com.alibaba.fastjson.TypeReference;

import mdmcloud.sdk.SdkContants;
import mdmcloud.sdk.bean.Page;
import mdmcloud.sdk.bean.User;

public class UserApi extends RestApi<User> {

	public UserApi() {
		super(SdkContants.BASE_URI + "/user", User.class, new TypeReference<Page<User>>() {
		});
	}

	public static void main(String[] args) {
		Page<User> page = new UserApi().page(0, 10, null);
		page.getRows().forEach(user -> System.out.println(user.getName()));
	}
}

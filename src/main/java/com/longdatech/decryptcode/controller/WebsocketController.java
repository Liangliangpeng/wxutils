package com.longdatech.decryptcode.controller;

import com.longdatech.decryptcode.utils.ServerResponse;
import com.longdatech.decryptcode.websocketutils.WebSocketServer;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @description webocket控制器
 * @author: liyinlong
 * @date 2019-06-11 10:44
 */

@Api(description = "Websocket控制器")
@RestController
@RequestMapping("/messagecenter")
public class WebsocketController {

	//页面请求
	@GetMapping("/socket/{cid}")
	public ModelAndView socket(@PathVariable String cid) {
		ModelAndView mav=new ModelAndView("/socket");
		mav.addObject("cid", cid);
		return mav;
	}

	//推送数据接口
	@ResponseBody
	@GetMapping("/socket/push/{cid}")
	public ServerResponse pushToWeb(@PathVariable String cid,String message) {
		try {
			WebSocketServer.sendInfo(200,message,cid);
		} catch (IOException e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage(cid+"#"+e.getMessage());
		}
		return ServerResponse.createBySuccess(cid);
	}
}
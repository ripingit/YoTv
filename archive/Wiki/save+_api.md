# YoTv API

* Auth

	用户相关接口，账号管理和个人设置。

 	| path | method | description |
	| - | - | - |
	| /user | POST | 新用户注册 |
	| /user | GET | 用户登录 |
	| /user | PUT | 修改密码 |
	| /user/avatar | PUT | 修改头像 |
	
* Record

	收支接口。
	
 	| path | method | description |
	| - | - | - |
	| /record | POST | 新建收支 |
	| /record/[record_id] | GET | 获取收支 |
	| /record/[record_id] | PUT | 修改收支 |
	| /record/[record_id] | DELETE | 删除收支 |
	| /record/[record_id]/reimburse | POST | 报销 |
	
* Iou

	未完成账目接口。
	
 	| path | method | description |
	| - | - | - |
	| /iou | POST | 新建iou |
	| /iou/[iou_id] | GET | 获取iou信息 |
	| /iou/[iou_id] | PUT | 修改iou |
	| /iou/[iou_id]/finish | POST | 完成iou |
	| /iou/[iou_id]/check | POST | 审核iou |
	| /iou/[iou_id]/confirm | POST | 同意iou |
	| /iou/[iou_id] | DELETE | 删除iou |
	
* Tag

	标签接口。

 	| path | method | description |
	| - | - | - |
	| /tag/[record_id] | POST | 新建标签 |
	| /tag/[record_id] | DELETE | 删除标签 |
	

	
* Book

	账本接口。
	
 	| path | method | description |
	| - | - | - |
	| /book | POST | 新建账本 |
	| /book/[book_id] | GET | 获取账本 |
	| /book/[book_id] | PUT | 修改账本 |
	| /book/record/[record_id] | POST | 加入新收支 |
	| /book/record/[record_id] | DELETE | 删除收支 |

* Org

	组织接口，组织管理、成员管理。
	
 	| path | method | description |
	| - | - | - |
	| /org | POST | 新建组织 |
	| /org | GET | 用户加入的组织 |
	| /org/[org_id] | GET | 获取某组织信息 |
	| /org/[org_id] | PUT | 修改组织 |
	| /org/member | GET | 组织成员 |
	| /org/member/[user_id] | POST | 加入新成员 |
	| /org/member/[user_id] | DELETE | 删除成员 |
	
* Wish

	愿望单接口。
	
 	| path | method | description |
	| - | - | - |
	| /wish | GET | 新建个人愿望单 |
	| /wish | POST | 新建愿望 |
	| /wish/[user_id] | GET | 获取某人愿望单 |
	| /wish/[wish_id] | PUT | 修改某个愿望 |
	| /wish/[wish_id] | DELETE | 删除某个愿望 |
	
* Sync

	同步接口。
	
	| path | method | description |
	| - | - | - |
	| /sync | GET | 拉取 |
	
* Friend

	好友接口。
	
	| path | method | description |
	| - | - | - |
	| /friend | GET | 好友列表 |
	| /friend | POST | 添加好友 |
	| /friend | PUT | 修改备注等 |
	| /friend | DELETE | 删除好友 |
	| /friend/[friend_id]/reminder | POST | 向好友发送提醒 |
	| /friend/[friend_id]/debt | GET | 与好友之间的债务 |
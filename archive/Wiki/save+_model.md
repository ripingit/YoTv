# YoTv Model

* 账本 book
	* ID uuid
	* 名称 name
	* 封面 cover
	* 创建者 userId
	* 公开 isPublic
	* 存钱目标 saveTarget
	* 创建时间 createTime
	* 更新时间 updateTime
* 收支表 recordSheet
	* 时间 createTime
	* 操作人 userId
	* 收支 recordId
	* 账本 bookId
* 收支 record
	* ID uuid
	* 时间 createTime	
	* 地点 place
	* 收支 io ('i' / 'o')
	* 交易额 amount
	* 附件（图片）attachment
	* 周期 isRegular
	* 周期规则 regularRule
	* 类型 type
	* 来源 source
	* 备注 comment
	* 创建者 userId
	* 报销 isReimbursed
* 用户 user
	* ID uuid
	* 账号 account
	* 密码 password
	* 盐 salt
* 用户信息 userInfo
	* ID userId
	* 账户等级 level
	* 昵称 nickname
	* 头像 avatar
	* 周预算 weekBudget
	* 月预算 monthBudget
	* 年预算 yearBudget
	* 公开愿望单 isWishPublic
	* 更新时间 updateTime
* 第三方账号 userBind
	* 账号 account
	* 平台 platform
	* 拥有者 userId
	* OAuth token
* 权限 bookAuth
	* 账本 bookId
	* 拥有者 ownerId
	* 拥有者类型 ownerType
	* 拥有者权限 ownerAuth ('r', 'w', 'x')
* 组织 org
	* 创建时间 createTime
	* 头像 avatar
	* 创建者 userId
	* 名称 name
* 组织成员 orgMember
	* 权限 auth
	* 加入时间 createTime
	* 成员 userId
	* 组织 orgId
* 标签 tag
	* 创建者 userId
	* 收支 recordId
	* 名称 name
	* 创建时间 createTime
* 未完成账目 iou（集资、借条）
	* 创建者 userId
	* 创建时间 createTime
	* 总金额 totalAmount
	* 类型 type
	* 付款人 payers
	* 均摊金额 shareAmount (对应payers)
	* 进度 state（x/count(payers)）
	* 核对名单 checkList ('userId': x/count(checkers))
	* 核对人 checkers
	* 已确认 confirm (对应payers，一旦确定，无法悔改)
	* 完成 isFinished
* 愿望单 wishList
	* ID id
	* 愿望 wish
	* 估价 price
	* 创建者 userId
	* 备注 comment
	* 类型 type
	* 愿望状态 state
	* 预计时间 predictPlan
* 变更表 change (用于多终端同步)
	* 操作者 userId
	* 变更对象 targetId
	* 变更类型 targetType ('C', 'U', 'D')
	* 操作类型 type
	* 操作时间 createTime
* 通知 notification
	* 通知对象 userId
	* 内容 content
	* 创建时间 createTime
	* 通知类型 type
	* 额外信息 extra
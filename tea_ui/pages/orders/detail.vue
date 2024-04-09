<template>
	<view class="container" style="padding:20rpx;">
		<view style="padding-bottom: 100rpx;">
			<view>
					<view class="bg-white section">
						<list-cell :hover="false" padding="50rpx 30rpx 20rpx" >
							<view class="items">
								<view class="pay-cell afterclass" style="background:#fff;font-size:14px;color:#5A5B5C;padding:15px 15px;">
									<view style="margin-bottom: 10rpx;" after>商品信息</view>
								</view>
								<view v-for="(pro, index) in orderInfo.productVOList" :key="index" class="goods pay-cell">
									<image :src="goodsImageBaseUrl + pro.imgUrl" class="image"></image>
									<view class="right">
										<text class="name">{{ pro.goodsDetail}}</text>
										<text class="name">X {{pro.goodsTotalNum}}</text>
										<text class="price">￥{{ pro.totalPrice / 100 }} </text>
									</view>
								</view>
								<view class="w-100 d-flex flex-column">
									<view class="pay-cell">
										<view style="margin-bottom: 10rpx;">总计：</view>
										<view class="font-weight-bold">￥{{ orderInfo.totalPrice/100 }}</view>
									</view>
								</view>
							</view>
						</list-cell>
					</view>
				<view class="section">
					<!-- order info begin -->
					<list-cell :hover="false" padding="50rpx 30rpx" last>
						<view class="w-100 d-flex flex-column">
							<view class="pay-cell">
								<view>订单编号</view>
								<view class="font-weight-bold">{{ orderInfo.orderNo }}</view>
							</view>
							<view class="pay-cell">
								<view>订单状态</view>
								<view class="font-weight-bold">{{ orderInfo.orderStatus }}</view>
							</view>
							<view class="pay-cell">
								<view>下单时间</view>
								<view class="font-weight-bold">{{ orderInfo.createTime }}</view>
							</view>
							<view class="pay-cell">
								<view>订单编号</view>
								<view class="font-weight-bold">{{ orderInfo.orderNo }}</view>
							</view>
							<view class="pay-cell">
								<view>取单号</view>
								<view class="font-weight-bold">{{ orderInfo.verifyNum }}</view>
							</view>
							<view class="pay-cell">
								<view>订餐方式</view>
								<view class="font-weight-bold">{{ orderInfo.takeType }}</view>
							</view>
							<view class="pay-cell">
								<view>完成时间</view>
								<view class="font-weight-bold">{{ orderInfo.finishTime ? orderInfo.finishTime : '未完成'}}</view>
							</view>
							<view class="pay-cell">
								<view>联系电话</view>
								<view class="font-weight-bold">{{ orderInfo.userPhone }}</view>
							</view>
							<view class="pay-cell">
								<view>取餐人</view>
								<view class="font-weight-bold">{{ orderInfo.receiver }}</view>
							</view>
							<view style="margin-bottom: 10rpx;">订单备注</view>
							<view class="pay-cell">
								<view class="font-weight-bold">{{ orderInfo.extraInfo }}</view>
							</view>
						</view>
					</list-cell>
					<!-- order info end -->
				</view>
			</view>
			<view class="position-relative w-100"><image src="/static/images/order/bottom.png" mode="widthFix" class="w-100"></image></view>
		</view>
	</view>
</template>

<script>
import Orders from '@/api/orders';
import listCell from '@/components/list-cell/list-cell';

export default {
	components: {
		listCell
	},

	data() {
		return {
			orderInfo: {},
			// 商品图片的基础路径
			goodsImageBaseUrl: this.$config.baseUrl + '/static/image/'
		};
	},
	onLoad({ orderNo }) {
		if (orderNo) this.getOrderInfo(orderNo);
	},
	methods: {
		getOrderInfo(orderNo) {
			this.$request('/order/detail/' + orderNo, 'get').then(result => {
				this.orderInfo = result.data;
				console.log(JSON.stringify(result.data));
			});
		},
		review() {
			uni.navigateTo({
				url: '/pages/review/review'
			});
		}
	}
};
</script>

<style lang="scss" scoped>
@mixin arch {
	content: '';
	position: absolute;
	background-color: $bg-color;
	width: 30rpx;
	height: 30rpx;
	bottom: -15rpx;
	z-index: 10;
	border-radius: 100%;
}

.section {
	position: relative;

	&::before {
		@include arch;
		left: -15rpx;
	}

	&::after {
		@include arch;
		right: -15rpx;
	}
}

.pay-cell {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	font-size: $font-size-base;
	color: $text-color-base;
	margin-bottom: 40rpx;

	&:nth-last-child(1) {
		margin-bottom: 0;
	}
}

.invote-box {
	position: absolute;
	width: 100%;
	left: 0;
	top: 0;
	display: flex;
	justify-content: center;
	align-items: center;

	image {
		width: 30rpx;
		height: 30rpx;
	}
}
.items {
	width: 100%;
}
.items image {
	width: 100px;
	height: 100px;
	margin-bottom: 20px;
}
.items .goods{
	    display: flex;
	    align-items: center;
	    margin-bottom: 30rpx;
}
.items .right{
	    flex: 1;
	    height: 100rpx;
	    overflow: hidden;
	    display: flex;
	    flex-direction: column;
	    align-items: flex-start;
	    justify-content: space-between;
	    padding-right: 14rpx;
		font-size: 28rpx;
		color: #5A5B5C;
		margin-bottom: 40rpx;
}
.items .name{
	font-size: 28rpx;
	margin-bottom: 10rpx;
	justify-content: end;
}
.items .price{
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
	margin-right: 10rpx;
	font-size: 28rpx;
	font-weight: 600;
}
</style>

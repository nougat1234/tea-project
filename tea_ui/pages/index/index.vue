<template>
	<view class="container">
		<view class="banner">
			<image src="https://img-shop.qmimg.cn/s23107/2020/04/26/3eb7808bf105262604.jpg" mode="" class="bg"></image>
			<view class="intro">
				<view class="note">一杯奶茶，一口软欧包，在奈雪遇见两种美好</view>
			</view>
		</view>
		<view class="content">
			<view class="entrance">
				<view class="item" @tap="takein">
					<image src="/static/images/index/zq.png" class="icon"></image>
					<view class="title">自取</view>
				</view>
				<view class="item" @tap="takein">
					<image src="/static/images/index/wm.png" class="icon"></image>
					<view class="title">外卖</view>
				</view>
			</view>
			<view class="navigators">
				<view class="left">
					<view class="grid flex-column just-content-center">
						<view class="d-flex align-items-center">
							<image src="/static/images/index/csc.png" class="mark-img"></image>
							<view class="font-size-sm text-color-base">奈雪的茶商城</view>
						</view>
						<view class="text-color-assist" style="margin-left: 40rpx; font-size: 20rpx;">优质茶礼盒，网红零食</view>
					</view>
					<view class="grid justify-content-end align-items-end">
						<image src="/static/images/index/yzclh.png" class="yzclh-img" mode="heightFix"></image>
					</view>
				</view>
				<view class="right">
					<view class="tea-activity" @tap="invite">
						<image src="/static/images/index/mcsb.png" class="mark-img"></image>
						<view>买茶送包</view>
						<view class="right-img">
							<image src="/static/images/index/mcsb_bg.png" mode="widthFix"></image>
						</view>
					</view>
					<view class="member-gifts" @tap="packages">
						<image src="/static/images/index/hyjb.png" class="mark-img"></image>
						<view>会员劵包</view>
						<view class="right-img">
							<image src="/static/images/index/hyjb_bg.png" mode="widthFix"></image>
						</view>
					</view>
				</view>
			</view>
			<view class="member-news">
				<view class="header">
					<view class="title">热销奶茶排行</view>
					<view class="iconfont iconRightbutton"></view>
				</view>				 
				<view class="list" style="background-color: white;">
					 <!-- 商品列表 begin -->
					<view class="item good" v-for="(goods, key) in goods[0].goodsList.slice(0,5)" :key="key" style="margin-bottom: 10px;margin-righr: 15px;">
						<image :src="goodsImageBaseUrl + goods.image" class="image" style="width: 40%;"></image>
						<view class="right">
							<text class="name">{{ goods.name }}</text>
							<text class="tips">{{ goods.description && goods.description != '' ? goods.description : goods.name }}</text>
							<view class="price_and_action">
								<text class="price">￥{{ goods.defaultPrice / 100 }}</text>
							</view>
						</view>
					</view>
					<!-- 商品 end -->
				</view>
				 
			</view>
			<view class="member-news">
				<view class="header">
					<view class="title">会员新鲜事</view>
					<view class="iconfont iconRightbutton"></view>
				</view>
				<view class="list">
					<view class="item" style="margin-bottom: 10px;">
						<image src="https://img-shop.qmimg.cn/s23107/2020/04/27/0039bf41c9ebd50a2c.jpg"></image>
					</view>
					<view class="item" style="margin-bottom: 10px;">
						<image src="https://news.sznews.com/pic/2023-03/30/30147190_e3ee3553-189c-47b7-91bb-44d2375b1180.jpeg"></image>
					</view>
					<view class="item" style="margin-bottom: 10px;">
						<image src="https://img2.baidu.com/it/u=826380917,2882070732&fm=253&fmt=auto&app=138&f=JPEG"></image>
					</view>
					<view class="item" style="margin-bottom: 10px;">
						<image src="https://img1.jiemian.com/101/original/20200511/158916592939923300_a640x364.jpg"></image>
					</view>
					<view class="item" style="margin-bottom: 10px;">
						<image src="https://img2.baidu.com/it/u=3685385072,122281026&fm=253&fmt=auto&app=138&f=JPEG"></image>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import modal from '@/components/modal/modal';
	import popupLayer from '@/components/popup-layer/popup-layer';
	import { mapState, mapMutations, mapActions, mapGetters } from 'vuex';
	import staticGoods from '../../api/goods.js';
	import staticStores from '../../api/store.js';
	export default {
		components: {
			modal,
			popupLayer
		},
		data() {
			return {
				
				goods: staticGoods, // 引入静态数据
				// 商品图片的基础路径
				goodsImageBaseUrl: this.$config.baseUrl + '/static/image/'
			}
		},
		computed: {
			...mapState(['member']),
			...mapGetters(['isLogin'])
		},
		onLoad() {
			this.init();
		},
		methods: {
			//页面初始化
			async init() {
				let that = this;
				that.$request('/goods/goodsMenu/list', 'get').then(result => {
					console.log('获取数据' + JSON.stringify(result.data));
					that.goods = result.data;
				});
			},
			takein() {
				this.$store.commit('SET_ORDER_TYPE', 'takein')
				uni.switchTab({
					url: '/pages/menu/menu'
				})
			},
			takeout() {
				if(!this.isLogin) {
					uni.navigateTo({url: '/pages/login/login'})
					return
				}
				uni.navigateTo({
					url: "/pages/address/address?is_choose=true"	
				})
			},
			integrals() {
				if(!this.isLogin) {
					uni.navigateTo({url: '/pages/login/login'})
					return
				}
				uni.navigateTo({
					url: '/pages/integrals/integrals'
				})
			},
			packages() {
				uni.navigateTo({
					url: '/pages/packages/index'
				})
			},
			memberCode() {
				if(!this.isLogin) {
					uni.navigateTo({url: '/pages/login/login'})
					return
				}
				uni.navigateTo({
					url: '/pages/mine/member-code'
				})
			},
			invite() {
				uni.navigateTo({
					url: '/pages/activities/invite'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
/* #ifdef H5 */
page {
	height: auto;
	min-height: 100%;
}
/* #endif */

.banner {
	position: relative;
	width: 100%;
	height: 600rpx;
	
	.bg {
		width: 100%;
		height: 600rpx;
	}
	
	.intro {
		position: absolute;
		top: calc(50rpx + var(--status-bar-height));
		left: 40rpx;
		color: #FFFFFF;
		display: flex;
		flex-direction: column;
		
		.greet {
			font-size: $font-size-lg;
			margin-bottom: 10rpx;
		}
		
		.note {
			font-size: $font-size-sm;
		}
	}
}

.content {
	padding: 0 30rpx;
}

.entrance {
	position: relative;
	margin-top: -80rpx;
	margin-bottom: 30rpx;
	border-radius: 10rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
	padding: 30rpx 0;
	display: flex;
	align-items: center;
	justify-content: center;
	
	.item {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		position: relative;
		
		&:nth-child(1):after {
			content: '';
			position: absolute;
			width: 1rpx;
			background-color: #ddd;
			right: 0;
			height: 100%;
			transform: scaleX(0.5) scaleY(0.8);
		}
		
		.icon {
			width: 84rpx;
			height: 84rpx;
			margin: 20rpx;
		}
		
		.title {
			font-size: 30rpx;
			color: $text-color-base;
			font-weight: 600;
		}
	}
}

.info {
	position: relative;
	margin-bottom: 30rpx;
	border-radius: 10rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
	padding: 30rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	
	.integral_section {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		
		.top {
			display: flex;
			align-items: center;
			
			.title {
				color: $text-color-base;
				font-size: $font-size-base;
				margin-right: 10rpx;
			}
			.value {
				font-size: 44rpx;
				font-weight: bold;
			}
		}
		
		.bottom {
			font-size: $font-size-sm;
			color: $text-color-assist;
			display: flex;
			align-items: center;
		}
	}
	
	.qrcode_section {
		color: $color-primary;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		font-size: $font-size-sm;
		
		image {
			width: 40rpx;
			height: 40rpx;
			margin-bottom: 10rpx;
		}
	}
}

.navigators {
	width: 100%;
	margin-bottom: 20rpx;	
	border-radius: 10rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
	padding: 20rpx;
	display: flex;
	align-items: stretch;
	
	.left {
		width: 340rpx;
		margin-right: 20rpx;
		display: flex;
		padding: 0 20rpx;
		flex-direction: column;
		font-size: $font-size-sm;
		color: $text-color-base;
		background-color: #F2F2E6;
		
		.grid {
			height: 50%;
			display: flex;
		}
	}
	
	.right {
		width: 290rpx;
		display: flex;
		flex-direction: column;
		
		.tea-activity, .member-gifts {
			width: 100%;
			display: flex;
			padding: 20rpx;
			font-size: $font-size-sm;
			color: $text-color-base;
			align-items: center;
			position: relative;
		}
		
		.tea-activity {
			background-color: #FDF3F2;
			margin-bottom: 20rpx;
		}
		
		.member-gifts {
			background-color: #FCF6D4;
		}
		
		.right-img {
			flex: 1;
			position: relative;
			margin-left: 20rpx;
			margin-right: -20rpx;
			margin-bottom: -20rpx;
			display: flex;
			align-items: flex-end;
			
			image {
				width: 100%;
			}
		}
	}
	
	.mark-img {
		width: 30rpx;
		height: 30rpx;
		margin-right: 10rpx;
	}
	
	.yzclh-img {
		height: 122.96rpx;
		width: 214.86rpx;
	}
}

.member-news {
	width: 100%;
	margin-bottom: 30rpx;
	.header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 0;
		
		.title {
			font-size: $font-size-lg;
			font-weight: bold;
		}
		
		.iconfont {
			font-size: 52rpx;
			color: $text-color-assist;
		}
	}
	
	.list {
		width: 100%;
		display: flex;
		flex-direction: column;
		
		.item {
			width: 100%;
			height: 240rpx;
			position: relative;
			
			image {
				width: 100%;
				height: 100%;
				border-radius: 8rpx;
			}
			
			.title {
				position: relative;
				font-size: 32rpx;
				font-weight: 500;
				width: 100%;
				top: -70rpx;
				left: 16rpx;
				color: #ffffff;
			}
		}
	}
	
	.good {
		display: flex;
		align-items: center;
		margin-bottom: 15px;
	}
	.goodImage {
		background-image: none;
		background-position: 0% 0%;
		background-size: 100% 100%;
		background-repeat: no-repeat;
	}
	.good .right{
		width: 50%;
		flex: 1;
		height: 80px;
		overflow: hidden;
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
		padding-right: 7px;
	}
	.name {
		font-size: 14px;
		margin-bottom: 5px;
	}
	.tips{
		width: 100%;
		height: 20px;
		line-height: 20px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		font-size: 12px;
		color: #919293;
		margin-bottom: 5px;
	}
	.price{
		font-size: 14px;
		font-weight: 600;
	}
}
</style>

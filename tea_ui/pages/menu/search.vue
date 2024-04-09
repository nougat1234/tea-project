<template>
	<view>
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="backText">搜索</block>
		</cu-custom>
		
		<view class="cu-bar search bg-white">
			<view class="search-form round">
				<text class="cuIcon-search"></text>
				<input @focus="InputFocus" @blur="InputBlur" :adjust-position="false" type="text" 
				placeholder="搜索门店/地址" confirm-type="search"></input>
			</view>
		</view>
		
		<rich-text :nodes="strings"></rich-text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				strings: '<p><b>测试内容</b></p><p><img src="https://qm-1251361737.cos.ap-shanghai.myqcloud.com/pro/p1586867309622.png" data-filename="" style="width: 780px;"></p><p><img src="https://qm-1251361737.cos.ap-shanghai.myqcloud.com/pro/p1586866495087.png" data-filename="" style="width: 1125px;"><b><br></b></p>'
			}
		},
		methods: {
			
		}
	}
</script>

<style>

</style>

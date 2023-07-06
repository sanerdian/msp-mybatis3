var myObj = {
	'sm4Code':'QTMjQEFhZHNmYWd1LmFzZw==',
	getSm4Code:function(){
		var base = new Base64();
		var result = base.decode(this.sm4Code);
		return result;
	},
	sm4Encode:function (text) {
		return SM4.encode({
			input:text,
			key: this.getSm4Code()
		});;
	},
	sm4Decode:function (text) {
		var result='';
		if(text){
			try{
				result=SM4.decode({
					input:text,
					key: this.getSm4Code()
				});
				if(result&&result.charCodeAt(result.length-1)==0){
					var end=0;
					for(var i=result.length-1;i>0;i--){
						if(result.charCodeAt(i)!=0){
							end=i+1;
							break;
						}
					}
					result=result.substring(0,end);
				}
			}catch (e) {
				result='';
			}
		}
		return  result;
	}
};


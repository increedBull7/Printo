class FileUpload
{
	//construtor for class
	constructor(filename,pObj,url,)
	{
		this.pObj = pObj
		this.url = url
		this.read(filename)
		this.audio = new Audio("audio/sent_a.wav")
	}

	//routine for reading file and converting to base64
	read(filename)
	{

		let reader = new FileReader()
		reader.addEventListener("load",event=>
		{

			// remove base64 encoding's metadata
			let base64 = reader.result.replace(/^data:.+;base64,/, '');
			this.upload(base64)
		})

		//read data in urlencoding(base64)
		reader.readAsDataURL(filename)
	}

	upload(data)
	{
		let xhr = new XMLHttpRequest()
		xhr.open("POST", this.url, true)

		//update progress bar
		xhr.upload.addEventListener("progress",event=>
		{
			let percent = (event.loaded/event.total)*100
			this.pObj.style.width = percent+"%"
		})

		//handle upload complete
		xhr.upload.addEventListener("loadend",event=>
		{
			this.audio.play()
		})

		//handle err
		xhr.upload.addEventListener('error', event=> 
		{

    	})

		//sending data to server
		xhr.send(new Blob([data]))
	}
}

export {FileUpload}
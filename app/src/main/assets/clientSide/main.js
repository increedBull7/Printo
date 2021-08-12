let url 
let file = document.querySelector(".file")
let pro = document.querySelector(".pro")

file.addEventListener("change",event=>
{
	let file = event.target.files[0]
	url = window.location.origin+"/"+ event.target.files[0].name
	read(file)
})
	
function read(file)
{
	let reader = new FileReader()
	reader.addEventListener("load",event=>
	{
		var b64 = reader.result.replace(/^data:.+;base64,/, '');
		main(b64)
	})
	reader.readAsDataURL(file)
}
	
function main(blob)
{	
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	
	xhr.upload.addEventListener("progress",event=>
	{
		let percent = (event.loaded/event.total)*100
		pro.value = percent
	})
	
	xhr.upload.addEventListener("loadend",event=>
	{
		let heading = document.createElement("h1")
		heading.textContent = "pass"
		heading.style.color="green"
		document.body.appendChild(heading);
	})
	
	xhr.upload.addEventListener('error', event=> 
	{

    })

	xhr.send(new Blob([blob]))
}

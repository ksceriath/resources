GL = Graphics Library

- Open GL is a standard graphics library for *talking* to a [[GPU]].
- Other alternatives are DirectX and Vulcan
- WebGL - also known as OpenGL ES (Embedded Systems) - is a light version of OpenGL, targeted at mobile devices, browsers, etc.


### OpenGL
- has a library - runs on the CPU, and sends stuff to GPU
- has shaders - the code that is supposed to run on GPU  (sent to GPU through the library part). In OpenGL, this code is written in GLSL (Graphics Library Shading Language)
	1. Vertex Shader : its a program that computes the position of a vertex. It is executed once for each vertex once.
	2. Fragment Shader : its a program that computes the colour of a pixel. It is executed once for each pixel to be rendered.


###### More details can be added to this.
(#draft)


#GraphicsProgramming #Graphics #GPU #GL



##### References
- https://engineering.monstar-lab.com/en/post/2022/03/01/Introduction-To-GPUs-With-OpenGL/
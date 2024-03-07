<div align="center">
    <a href="https://github.com/romanamo/fractolio">
        <img src="src/main/resources/logo.png" alt="Logo" width="128" height="128" style="background:transparent">
    </a>
    <h3 align="center">Explorino</h3>
</div>

## About

Explorino is a tool to explore a variety of Fractals by simple moving or zooming.

Precision goes as far as a 64-bit floating-point number for a zoomed in point allows.

You can save your favorite findings as `.png`-image.
Resolution and Computation depth can be selected.

Language Support for:

- English
- French
- German
- Spanish

## Requirements

Java 11 or higher is required.

## Controls

In Fullscreen mode following keys can be used to navigate around.

| Key(s) | Action                                     |
|--------|--------------------------------------------|
| R      | Reset Offset to Origin (0,0) and Zoom to 1 |
| WASD   | Up-/Left-/Down-/Right-Movement             |
| IO     | In-/Out-Zoom                               |

### Fractals

Explorino supports following fractals:

| Fractal              | Description                                    | Options                         |
|----------------------|------------------------------------------------|---------------------------------|
| Mandelbrot/Multibrot | https://en.wikipedia.org/wiki/Mandelbrot_set   | - degree                        |
| Julia/Multijulia     | https://en.wikipedia.org/wiki/Julia_set        | - degree<br>- parameter c       |
| Newton/Nova          | https://en.wikipedia.org/wiki/Newton_fractal   | - up to 5 variable coefficients |
| Lyapunov             | https://en.wikipedia.org/wiki/Lyapunov_fractal | - sequence                      |

### Coloring

Explorino supports following coloring methods:

| Coloring                | Description                                                                | Options  |
|-------------------------|----------------------------------------------------------------------------|----------|
| Palette Coloring        | Coloring based on convergence given by a pre choosen custom color palette. |          |
| Argument Coloring       | Coloring based of the complex Argument of the last iteration.              | - invert |
| Absolute Value Coloring | Coloring based of the absolute value of the last iteration.                | - invert |
| Basic Coloring          | Coloring based on convergence and Hue of the HSV Model.                    |          |

## Gallery

<p>
<img src="gallery/julia_gallery.png" alt="julia fractal" width="45%">
<img src="gallery/newton_gallery.png" alt="newton fractal" width="45%">
<img src="gallery/mandelbrot_gallery.png" alt="mandelbrot fractal" width="45%">
<img src="gallery/lyapunov_gallery.png" alt="lyapunov fractal" width="45%">
</p>

## Built with

- [JavaFx](https://openjfx.io/)
- [Maven](https://maven.apache.org/)

## License

MIT License


[CmdletBinding()] Param (
        [Parameter(Position = 0, Mandatory = $True)]
        [String]
        $Image,

        [Parameter(Position = 1, Mandatory = $True)]
        [String]
        $Out
)
 
$ErrorActionPreference = "Stop" 
  
[void] [System.Reflection.Assembly]::LoadWithPartialName("System.Drawing")

if (-Not [System.IO.Path]::IsPathRooted($Image)){
        $Image = [System.IO.Path]::GetFullPath((Join-Path (pwd) $Image))
    }
if (-Not [System.IO.Path]::IsPathRooted($Out)){
        $Out = [System.IO.Path]::GetFullPath((Join-Path (pwd) $Out))
    }

$img = New-Object System.Drawing.Bitmap($Image)

$width = $img.Size.Width
$height = $img.Size.Height

$rect = New-Object System.Drawing.Rectangle(0, 0, $width, $height);
$bmpData = $img.LockBits($rect, [System.Drawing.Imaging.ImageLockMode]::ReadWrite, $img.PixelFormat)
$lmaxpayload  = [Math]::Abs($bmpData.Stride) * $img.Height / 2
$img.UnlockBits($bmpData)
$img.Dispose()

$rows = [math]::Ceiling($lmaxpayload/$width)
$array = ($rows*$width)
$lrows = ($height-1)
$lwidth = ($width-1)
$lpayload = ($lmaxpayload-1)

$payload = ""
    
iex("sal a New-Object;Add-Type -AssemblyName `"System.Drawing`";`$g=a System.Drawing.Bitmap(`"$Image`");`$o=a Byte[] $array;(0..$lrows)|%{foreach(`$x in(0..$lwidth)){`$p=`$g.GetPixel(`$x,`$_);`$o[`$_*$width+`$x]=([math]::Floor((`$p.B-band15)*16)-bor(`$p.G-band15))}};`$g.Dispose();`$payload=[System.Text.Encoding]::ASCII.GetString(`$o[0..$lpayload])")
    
Out-File $Out -inputobject $payload
    
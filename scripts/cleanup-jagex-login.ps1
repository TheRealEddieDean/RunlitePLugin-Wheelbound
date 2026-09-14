$ErrorActionPreference = 'Stop'
$failed = $false

# Never read or print the credentials file. Remove only this exact file.
$credentialsPath = Join-Path $env:USERPROFILE '.runelite\credentials.properties'
$settingsPath = Join-Path $env:LOCALAPPDATA 'RuneLite\settings.json'

try {
    if (Test-Path -LiteralPath $settingsPath -PathType Leaf) {
        $settings = Get-Content -LiteralPath $settingsPath -Raw | ConvertFrom-Json
        $argumentsProperty = $settings.PSObject.Properties['clientArguments']
        if ($null -ne $argumentsProperty) {
            if ($argumentsProperty.Value -isnot [Array]) {
                throw 'Unexpected clientArguments format. Remove the argument in RuneLite (configure) manually.'
            }
            $remaining = @($argumentsProperty.Value | Where-Object {
                $_ -ne '--insecure-write-credentials'
            })
            if ($remaining.Count -ne $argumentsProperty.Value.Count) {
                $settings.clientArguments = $remaining
                $json = $settings | ConvertTo-Json -Depth 100
                [System.IO.File]::WriteAllText($settingsPath, $json, (New-Object System.Text.UTF8Encoding($false)))
                Write-Host 'Removed the credential-saving argument from RuneLite settings.'
            } else {
                Write-Host 'Credential-saving argument is already absent.'
            }
        } else {
            Write-Host 'No client arguments are configured.'
        }
    } else {
        Write-Warning 'RuneLite settings were not found. Check RuneLite (configure) manually.'
        $failed = $true
    }
} catch {
    Write-Warning 'Could not update RuneLite settings. Remove --insecure-write-credentials manually in RuneLite (configure).'
    $failed = $true
}

# Attempt credential removal even if updating the launcher settings failed.
try {
    if (Test-Path -LiteralPath $credentialsPath) {
        Remove-Item -LiteralPath $credentialsPath -Force
        Write-Host 'Deleted the saved development login file.'
    } else {
        Write-Host 'No saved development login file exists at the default location.'
    }
} catch {
    Write-Warning 'Could not delete the login file. Close RuneLite and check permissions, then run this script again.'
    $failed = $true
}

Write-Host ''
Write-Host 'To invalidate existing login credentials, use End sessions in your account settings on runescape.com.'
Write-Host 'This script does not revoke sessions or remove copies saved elsewhere.'
if ($failed) { exit 1 }
exit 0

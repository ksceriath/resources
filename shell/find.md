>> find /path options

{
    e.g. find / -name abc.xyz

    options = {
        -name filename   :  find filename
    
        -iname filename  :  (case-insensitive) find filename

        -user username   :  find files belonging to user username

        -group groupname :  find files belonging to group groupname
        
        -type f          :  find 'files'
              d          :  find 'directories'
              l          :  find 'symbolic links'
              c          :  find 'character devices'
              b          :  find 'block devices'

        -size n          :  find files with size = n * 512 bytes
              nc         :  find files with size = n bytes
              nk         :  find files with size = n kB
              nM         :  find files with size = n MB
              nG         :  find files with size = n GB
              nT         :  find files with size = n TB
              nP         :  find files with size = n PB
             +nX         :  find files where size > n X (where X : [c,k,M,G,T,P])
             -nX         :  find files where size < n X (where X : [c,k,M,G,T,P])

        -o               :  (or) combine multiple find options ( find / -name "abc" -o -name "def" )

        -delete          :  delete files found in the results
    }

    *character devices : (serial ports, parallel ports, sounds cards)
                       : the Driver communicates by sending and receiving single characters
    *block devices     : (hard disks, USB cameras, Disk-On-Key)
                       : the Driver communicates by sending entire blocks of data
}

[ find in multiple paths ]

>> find /path1 /path2 options

[ execute some command on the results ]

>> find /path -name "abc.xyz" -exec mv '{}' /path2 \;
>> find /path -name "abc.xyz" -exec echo '{}' +

{
    -exec   :   specify the command to execute on the found files

    {}    -   gets replaced with the file found
    +       -   all files are placed in one '{}' -- execute just one command
    ;       -   only one file is placed in one '{}' -- execute separate command for each result (need to escape ; )

}

[ multiple searches can be combined using parenthesis ]

>> find /path \( -name "abc*" -o -name "def*" -o -name "ghi*" \) -exec chmod a+x {} +

{
    if options are combined above using parenthesis, then exec operates only on the results of the last option.
}

[ execute with check ]

>> find /path -name "abc.xyz" -ok mv '{}' /path2 \;
{
    -ok     :   like -exec, but prompts for confimation before running the command
}
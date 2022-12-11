# U-Skripts

U-Skripts is a collection of kotlin "scripts" that I use in my personal environment. It is not intended 
to be used by anyone else, but they're shared for inspiration and guidance for others who want to build 
similar solutions.

*NOTICE*: These are untested AD-HOC scripts, and are not intended to be used in production.

## Scripts

### `u-notes`
An Obsidian vault navigator that open and create files in my vault. This script is capable of
creating new files based on a given template, and will try to guess the correct entry point for 
a given location. Please do note my vault is structured using a customised version of Johnny
Decimal system.

Example usage:
```bash
# Creates a runnable jar for the u-notes script 
./gradlew notes

# Opens my notes in Obsidian
function u-notes() {
    java -jar path/to/u-skripts/build/libs/u-notes.jar $1 $2 | xargs $OS_OPEN_CMD &>/dev/null;
}

# Some usage inputs/outputs
u-notes                             # Opens the root of my vault (i.e. index.md)
u-notes _                           # Opens today's daily note
u-notes _ 2022-12-22                # Opens/Creates a daily note by given date
u-notes 10                          # Opens the entry point for bucket 10
u-notes 42                          # Opens the entry point for category 42
u-notes 01 Obsidian                 # Opens category 01 file named "Obsidian.md"
u-notes 31 "A New Entry" Definition # Opens/Creates category 31 file named "A New Entry.md" using template "Definition.md"
u-notes -p 01 Obsidian | vi         # Opens category 01 file named "Obsidian.md" in vi
```

If the filename provided does not exist in given location, it will be created using the category's template file. (or 
the default template if the category does not have one) The script is also a little defensive, and will validate
certain values before opening the file.

___
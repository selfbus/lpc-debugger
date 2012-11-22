package org.freebus.debugger.gui;

import java.io.File;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;

/**
 * A glob file filter.
 */
public class GlobFileFilter extends FileFilter
{
   private final String description;
   private final Pattern[] patterns;

   public GlobFileFilter(String description, String filter)
   {
      this(description, new String[] { filter });
   }

   public GlobFileFilter(String description, String[] filters)
   {
      this.description = description;

      this.patterns = new Pattern[filters.length];
      for (int i = 0; i < filters.length; i++)
         this.patterns[i] = globToRegexp(filters[i]);
   }

   public boolean accept(File file)
   {
      if (file == null)
      {
         return false;
      }
      if (file.isDirectory())
      {
         return true;
      }
      if (!file.isFile())
      {
         return false;
      }
      for (Pattern pattern : this.patterns)
      {
         if (pattern.matcher(file.getName()).matches())
         {
            return true;
         }
      }
      return false;
   }

   public String getDescription()
   {
      return this.description;
   }

   /**
    * Convert the glob pattern to a regexp pattern.
    *
    * @param globPattern - the glob pattern.
    * @return The regexp pattern
    */
   public static Pattern globToRegexp(String globPattern)
   {
      char[] gPat = globPattern.toCharArray();
      char[] rPat = new char[gPat.length * 2];
      boolean isWin32 = File.separatorChar == '\\';
      boolean inBrackets = false;
      int j = 0;

      if (isWin32)
      {
         int len = gPat.length;
         if (globPattern.endsWith("*.*"))
         {
            len -= 2;
         }
         for (int i = 0; i < len; i++)
         {
            switch (gPat[i])
            {
               case '*':
                  rPat[(j++)] = '.';
                  rPat[(j++)] = '*';
                  break;
               case '?':
                  rPat[(j++)] = '.';
                  break;
               case '\\':
                  rPat[(j++)] = '\\';
                  rPat[(j++)] = '\\';
                  break;
               default:
                  if ("+()^$.{}[]".indexOf(gPat[i]) >= 0)
                  {
                     rPat[(j++)] = '\\';
                  }
                  rPat[(j++)] = gPat[i];
            }
         }

      }
      else
      {
         for (int i = 0; i < gPat.length; i++)
         {
            switch (gPat[i])
            {
               case '*':
                  if (!inBrackets)
                  {
                     rPat[(j++)] = '.';
                  }
                  rPat[(j++)] = '*';
                  break;
               case '?':
                  rPat[(j++)] = (inBrackets ? 63 : '.');
                  break;
               case '[':
                  inBrackets = true;
                  rPat[(j++)] = gPat[i];

                  if (i < gPat.length - 1)
                  {
                     switch (gPat[(i + 1)])
                     {
                        case '!':
                        case '^':
                           rPat[(j++)] = '^';
                           i++;
                           break;
                        case ']':
                           rPat[(j++)] = gPat[(++i)];
                     }
                  }

                  break;
               case ']':
                  rPat[(j++)] = gPat[i];
                  inBrackets = false;
                  break;
               case '\\':
                  if ((i == 0) && (gPat.length > 1) && (gPat[1] == '~'))
                  {
                     rPat[(j++)] = gPat[(++i)];
                  }
                  else
                  {
                     rPat[(j++)] = '\\';
                     if ((i < gPat.length - 1) && ("*?[]".indexOf(gPat[(i + 1)]) >= 0))
                     {
                        rPat[(j++)] = gPat[(++i)];
                     }
                     else
                     {
                        rPat[(j++)] = '\\';
                     }
                  }
                  break;
               default:
                  if (!Character.isLetterOrDigit(gPat[i]))
                  {
                     rPat[(j++)] = '\\';
                  }
                  rPat[(j++)] = gPat[i];
            }
         }
      }

      return Pattern.compile(new String(rPat, 0, j), 2);
   }
}

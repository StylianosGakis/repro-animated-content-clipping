Using a column with an animating in and out bottom navigation tab.
And a NavHost on top of it, which fills the rest of the column's space.
It takes the correct space as the red borders show, but somehow the height of the content gets clipped in an odd way as the available space to it increases. 

The transitions for the NavHost are set to EnterTransition.None and ExitTransition.None so the animation shouldn't affect how the content is laid out at all.

And this video shows the interaction live
<img alt="Repro" src="/misc/animation_clipping.gif" width="30%"/>

This picture captures the issue in the middle of the animation whee this clipping happens
![issue_snip.png](misc/issue_snip.png)
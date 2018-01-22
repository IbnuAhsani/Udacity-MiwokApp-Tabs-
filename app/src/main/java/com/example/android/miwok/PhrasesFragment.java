package com.example.android.miwok;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.AUDIO_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    /** Handles playback of all the sound files */
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
                {
                    releaseMediaPlayer();
                }
        };

    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener()
        {
            @Override
            public void onAudioFocusChange(int i)
                {
                    if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                        {
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0);
                        }
                    else if(i == AudioManager.AUDIOFOCUS_GAIN)
                        {
                            mediaPlayer.start();
                        }
                    else if(i == AudioManager.AUDIOFOCUS_LOSS)
                        {
                            releaseMediaPlayer();
                        }
                }
        };


    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer()
        {
            // If the media player is not null, then it may be currently playing a sound.
            if (mediaPlayer != null)
                {
                    // Regardless of the current state of the media player, release its resources
                    // because we no longer need it.
                    mediaPlayer.release();

                    // Set the media player back to null. For our code, we've decided that
                    // setting the media player to null is an easy way to tell that the media player
                    // is not configured to play an audio file at the moment.
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
        }

    public PhrasesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.word_list, container, false);

            audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);

            // Create a list of words
            final ArrayList<Word> words = new ArrayList<Word>();
            words.add(new Word("Where are you going?", "Minto Wuksus", R.raw.phrase_where_are_you_going));
            words.add(new Word("What is your name?", "Tinnә Oyaase'nә", R.raw.phrase_what_is_your_name));
            words.add(new Word("My name is...", "Oyaaset...", R.raw.phrase_my_name_is));
            words.add((new Word("How are you feeling?", "Michәksәs?", R.raw.phrase_how_are_you_feeling)));
            words.add(new Word("I'm feeling good", "Kuchi Achit", R.raw.phrase_im_feeling_good));
            words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
            words.add((new Word("Yes, I'm coming", "Hәә’ әәnәm", R.raw.phrase_yes_im_coming)));
            words.add(new Word("I'm coming", "әәnәm", R.raw.phrase_im_coming));
            words.add(new Word("Let's go", "Yoowutis", R.raw.phrase_lets_go));
            words.add(new Word("Come here", "әnni'nem", R.raw.phrase_come_here));

            // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
            // adapter knows how to create layouts for each item in the list, using the
            // simple_list_item_1.xml layout resource defined in the Android framework.
            // This list item layout contains a single {@link TextView}, which the adapter will set to
            // display a single word.
            WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

            // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
            // There should be a {@link ListView} with the view ID called list, which is declared in the
            // activity_numbers.xml layout file.
            ListView listView = (ListView) rootView.findViewById(R.id.list);

            // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
            // {@link ListView} will display list items for each word in the list of words.
            // Do this by calling the setAdapter method on the {@link ListView} object and pass in
            // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
            listView.setAdapter(adapter);

            // Set a click listener to play the audio when the list item is clicked on
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            // Get the {@link Word} object at the given position the user clicked on
                            Word word = words.get(i);

                            releaseMediaPlayer();

                            int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                            if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                                {
                                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                                    // with the current word
                                    mediaPlayer = MediaPlayer.create(getActivity(), word.getSoundResourceID());

                                    // Start the audio file
                                    mediaPlayer.start();

                                    mediaPlayer.setOnCompletionListener(completionListener);
                                }
                        }
                }
            );

            return rootView;
        }

    @Override
    public void onStop()
        {
            super.onStop();
            releaseMediaPlayer();
        }
}
